package org.sbolstandard.core.rdf;

import org.sbolstandard.core.SBOLObject;
import org.sbolstandard.core.rdf.impl.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Helpful utility for building picklers.
 *
 * <p>
 *   This class is intended for static import. It contains factory methods to wrap building up complex picklers from
 *   simple ones. By wrapping these in static factory methods, Java is able to do most of the type inference for you.
 * </p>
 *
 * @author Matthew Pocock
 */
public class RdfPicklers {

  /**
   * A resource maker that uses {@link org.sbolstandard.core.SBOLObject#getURI()}.
   */
  public static final ResourceMaker<SBOLObject> identity = new ResourceFromSBOLObjectUri();

  /**
   * Build a {@link org.sbolstandard.core.rdf.PropertyMaker} from the property name (URL).
   *
   * @param propName  the property name
   * @return          a PropertyMaker that will use this name to make properties
   */
  public static PropertyMaker property(String propName) {
    return new ByName(propName);
  }

  /**
   * Build a {@link org.sbolstandard.core.rdf.RdfEntityPickler} that applies each of a series of picklers in turn.
   *
   * @param picklers  the picklers to apply
   * @param <E>       the entity type
   * @return          a pickler that applies each of {@code picklers}
   */
  @SafeVarargs
  public static <E> RdfEntityPickler<E> all(RdfEntityPickler<? super E> ... picklers) {
    return new AllEntityPicklers<>(picklers);
  }

  /**
   * Build a {@link org.sbolstandard.core.rdf.RdfEntityPickler} that emits an RDF type assertion.
   *
   * @param type          the {@code type} of the entity
   * @param fromResource  resource maker for {@code E}
   * @param <E>           the entity type
   * @return              a pickler that emits this type assertion
   */
  public static <E> RdfEntityPickler<E> type(String type, ResourceMaker<? super E> fromResource) {
    return new WithType<>(fromResource, type);
  }

  /**
   * Build a {@link org.sbolstandard.core.rdf.RdfEntityPickler} that will emit a property assertion using introspection
   * to look up the property value.
   *
   * @param cls                       the entity Class
   * @param propName                  the name of the bean property
   * @param relationshipPickler       a pickler for the property
   * @param <E>                       the entity type
   * @param <P>                       the property value type
   * @return                          a pickler that pickles the property
   * @throws IntrospectionException   if {@code propName} can't be successfully resolved to a getter
   */
  @SuppressWarnings("unchecked")
  public static <E, P> RdfEntityPickler<E> byProperty(
          Class<E> cls, String propName, RdfPropertyPickler<? super E, P> relationshipPickler) throws IntrospectionException {
    BeanInfo bi = Introspector.getBeanInfo(cls);
    for(PropertyDescriptor pd : bi.getPropertyDescriptors()) {

      final Method readMethod = pd.getReadMethod();
      if(pd.getName().equals(propName) && readMethod != null) {
        return new ByProperty<E, P>(relationshipPickler) {
          @Override
          protected P target(E entity) {
            try {
              return (P) readMethod.invoke(entity); // note: suppressed warnings for this unchecked cast
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new IllegalArgumentException(e);
            }
          }
        };
      }
    }

    throw new IllegalArgumentException("No property of name " + propName + " found in " + cls);
  }

  /**
   * Build a {@link RdfPropertyPickler} that applies a series of picklers in turn.
   *
   * @param picklers  the picklers to apply
   * @param <E>       the entity type
   * @param <P>       the property type
   * @return          a pickler that applies each of {@code picklers}
   */
  @SafeVarargs
  public static <E, P> RdfPropertyPickler<E, P> all(RdfPropertyPickler<? super E, P>... picklers) {
    return new AllPropertyPicklers<>(picklers);
  }

  /**
   * Build a {@link RdfPropertyPickler} that pickles each value in a collection.
   *
   * @param wrapped   a pickler for each individual value
   * @param <E>       the entity type
   * @param <P>       the type of each element of the collection
   * @return          a pickler that pickles each element of a collection
   */
  public static <E, P> RdfPropertyPickler<E, Collection<P>> collection(RdfPropertyPickler<E, P> wrapped) {
    return new ACollection<>(wrapped);
  }

  /**
   * Build a {@link RdfPropertyPickler} that validates that the value is not null before
   * delegating on to another pickler.
   *
   * @param wrapped   the pickler to delegate on to
   * @param <E>       the entity type
   * @param <P>       the property value type
   * @return          a pickler that checks for nulls and delegates on to {@code wrapped}
   */
  public static <E, P> RdfPropertyPickler<E, P> notNull(RdfPropertyPickler<? super E, P> wrapped) {
    return new IsNotNull<>(wrapped);
  }

  /**
   * Build a {@link RdfPropertyPickler} that skips null values.
   *
   * @param wrapped   the pickler to delegate on to for non-null values
   * @param <E>       the entity type
   * @param <P>       the property value type
   * @return          a null-safe pickler that uses {@code wrapped} for non-null values
   */
  public static <E, P> RdfPropertyPickler<E, P> nullable(RdfPropertyPickler<E, P> wrapped) {
    return new IsNullable<>(wrapped);
  }

  /**
   * Build a {@link RdfPropertyPickler} that pickles an object property.
   *
   * @param fromResource  the resource maker for the entity owning the property
   * @param property      the property maker for the property
   * @param toResource    the resource maker for the property value
   * @param <E>           the entity type
   * @param <P>           the property value type
   * @return              a pickler that emits a subject, predicate, object assertion
   */
  public static <E, P> RdfPropertyPickler<E, P> object(ResourceMaker<? super E> fromResource, PropertyMaker property, ResourceMaker<? super P> toResource) {
    return new AnObject<>(fromResource, property, toResource);
  }

  /**
   * Build a {@link RdfPropertyPickler} that pickles a value property.
   *
   * @param fromResource  the resource maker for the entity owning the property
   * @param property      the property maker for the property
   * @param <E>           the entity type
   * @param <P>           the property value type
   * @return              a pickler that emits a subject, predicate, value assertion
   */
  public static <E, P> RdfPropertyPickler<E, P> value(ResourceMaker<? super E> fromResource, PropertyMaker property) {
    return new AValue<E, P>(fromResource, property) {
      @Override
      public String toAsString(P to) {
        return to.toString();
      }
    };
  }

  /**
   * Build a {@link RdfPropertyPickler} that pickles the value of the property.
   *
   * <p>
   *   This can be used to walk an object graph.
   *   Typically, {@link #all(RdfPropertyPickler[])} will wrap a call to
   *   {@link #object(ResourceMaker, PropertyMaker, ResourceMaker)} to link the entity with the property value,
   *   and {@code walkTo(RdfEntityPickler)} to emit the value.
   * </p>
   *
   * @param entityPickler a pickler to use for the property value
   * @param <E>           the entity type
   * @param <P>           the property value type
   * @return              a pickler that delegates to {@code entityPickler} to pickle the property value
   */
  public static <E, P> RdfPropertyPickler<E, P> walkTo(RdfEntityPickler<P> entityPickler) {
    return new WalkTo<>(entityPickler);
  }

}
