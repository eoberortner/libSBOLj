package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

import java.util.Collection;

/**
 * Pickler for Java properties to RDF relationships.
 *
 * Part of the pickler API to take some of the pain out of transforming Java objects to RDF representations.
 *
 * @author Matthew Pocock
 */
public interface RdfRelationshipPickler<E, P> {
  /**
   * Add RDF for a java relationship to a model.
   *
   * @param model   the Model to add to
   * @param from    the Java object that is the source of the property
   * @param to      the Java object that is the target of the property
   */
  public void pickle(Model model, E from, P to);

  public static abstract class AValue<E, P> implements RdfRelationshipPickler<E, P> {
    private final ResourceMaker<? super E> fromResource;
    private final PropertyMaker propertyMaker;

    protected AValue(ResourceMaker<? super E> fromResource, PropertyMaker propertyMaker) {
      this.fromResource = fromResource;
      this.propertyMaker = propertyMaker;
    }

    public abstract String toAsString(P to);
    
    @Override
    public void pickle(Model model, E from, P to) {
      model.add(fromResource.asResource(model, from), propertyMaker.propertyFor(model), toAsString(to));
    }
  }

  public static final class AnObject<E, P> implements RdfRelationshipPickler<E, P> {
    private final ResourceMaker<? super E> fromResource;
    private final PropertyMaker property;
    private final ResourceMaker<? super P> toResource;

    protected AnObject(ResourceMaker<? super E> fromResource, PropertyMaker property, ResourceMaker<? super P> toResource) {
      this.fromResource = fromResource;
      this.property = property;
      this.toResource = toResource;
    }

    @Override
    public void pickle(Model model, E from, P to) {
      model.add(fromResource.asResource(model, from), property.propertyFor(model), toResource.asResource(model, to));
    }
  }

  public static final class IsNotNull<E, P> implements RdfRelationshipPickler<E, P> {
    private final RdfRelationshipPickler<E, P> wrapped;

    public IsNotNull(RdfRelationshipPickler<E, P> wrapped) {
      this.wrapped = wrapped;
    }

    @Override
    public void pickle(Model model, E from, P to) {
      if(to == null) throw new NullPointerException(
              "Can't add a null property value for property");
      wrapped.pickle(model, from, to);
    }
  }

  public static final class IsNullable<E, P> implements RdfRelationshipPickler<E, P> {
    private final RdfRelationshipPickler<E, P> wrapped;

    public IsNullable(RdfRelationshipPickler<E, P> wrapped) {
      this.wrapped = wrapped;
    }

    @Override
    public void pickle(Model model, E from, P to) {
      if(to != null) {
        wrapped.pickle(model, from, to);
      }
    }
  }

  public static final class ACollection<E, P> implements RdfRelationshipPickler<E, Collection<P>> {
    private final RdfRelationshipPickler<E, P> wrapped;

    public ACollection(RdfRelationshipPickler<E, P> wrapped) {
      this.wrapped = wrapped;
    }

    @Override
    public void pickle(Model model, E from, Collection<P> to) {
      for(P p: to) {
        wrapped.pickle(model, from, p);
      }
    }
  }
  
  public static final class All<E, P> implements RdfRelationshipPickler<E, P> {
    private final RdfRelationshipPickler<? super E, P>[] picklers;

    public All(RdfRelationshipPickler<? super E, P>[] picklers) {
      this.picklers = picklers;
    }

    @Override
    public void pickle(Model model, E from, P to) {
      for(RdfRelationshipPickler<? super E, P> p : picklers) {
        p.pickle(model, from, to);
      }
    }
  }

  public static final class WalkTo<E, P> implements RdfRelationshipPickler<E, P> {
    private final RdfEntityPickler<P> entityPickler;

    public WalkTo(RdfEntityPickler<P> entityPickler) {
      this.entityPickler = entityPickler;
    }

    @Override
    public void pickle(Model model, E from, P to) {
      entityPickler.pickle(model, to);
    }
  }
}
