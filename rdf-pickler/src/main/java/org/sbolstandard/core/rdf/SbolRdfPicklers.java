package org.sbolstandard.core.rdf;

import org.sbolstandard.core.SBOLObject;

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
public class SbolRdfPicklers {

  public static final ResourceMaker<SBOLObject> identity = new ResourceMaker.FromIdentity();

  public static PropertyMaker property(String propName) {
    return new PropertyMaker.ByName(propName);
  }

  public static <E> RdfEntityPickler<E> all(RdfEntityPickler<? super E> ... picklers) {
    return new RdfEntityPickler.All<E>(picklers);
  }

  public static <E> RdfEntityPickler<E> type(String type, ResourceMaker<? super E> fromResource) {
    return new RdfEntityPickler.WithType<>(type, fromResource);
  }

  public static <E, P> RdfEntityPickler<E> byProperty(Class<E> cls, String propName, RdfRelationshipPickler<E, P> relationshipPickler) throws IntrospectionException {
    BeanInfo bi = Introspector.getBeanInfo(cls);
    for(PropertyDescriptor pd : bi.getPropertyDescriptors()) {

      final Method readMethod = pd.getReadMethod();
      if(pd.getName().equals(propName) && readMethod != null) {
        return new RdfEntityPickler.ByProperty<E, P>(relationshipPickler) {
          @Override
          protected P target(E entity) {
            try {
              return (P) readMethod.invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new IllegalArgumentException(e);
            }
          }
        };
      }
    }

    throw new IllegalArgumentException("No property of name " + propName + " found in " + cls);
  }

  public static <E, P> RdfRelationshipPickler<E, P> all(RdfRelationshipPickler<? super E, P>... picklers) {
    return new RdfRelationshipPickler.All<>(picklers);
  }

  public static <E, P> RdfRelationshipPickler<E, Collection<P>> collection(RdfRelationshipPickler<E, P> wrapped) {
    return new RdfRelationshipPickler.ACollection<>(wrapped);
  }

  public static <E, P> RdfRelationshipPickler<E, P> notNull(RdfRelationshipPickler<E, P> wrapped) {
    return new RdfRelationshipPickler.IsNotNull<>(wrapped);
  }

  public static <E, P> RdfRelationshipPickler<E, P> nullable(RdfRelationshipPickler<E, P> wrapped) {
    return new RdfRelationshipPickler.IsNullable<>(wrapped);
  }

  public static <E, P> RdfRelationshipPickler<E, P> object(ResourceMaker<? super E> fromResource, PropertyMaker property, ResourceMaker<? super P> toResource) {
    return new RdfRelationshipPickler.AnObject<>(fromResource, property, toResource);
  }

  public static <E, P> RdfRelationshipPickler<E, P> value(ResourceMaker<? super E> fromResource, PropertyMaker property) {
    return new RdfRelationshipPickler.AValue<E, P>(fromResource, property) {
      @Override
      public String toAsString(P to) {
        return to.toString();
      }
    };
  }

  public static <E, P> RdfRelationshipPickler<E, P> walkTo(RdfEntityPickler<P> entityPickler) {
    return new RdfRelationshipPickler.WalkTo<>(entityPickler);
  }

}
