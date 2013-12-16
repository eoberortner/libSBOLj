package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Pickler for Java objects to RDF entities.
 *
 * Part of the pickler API to take some of the pain out of transforming Java objects to RDF representations.
 *
 * @param  <E> the Java type to transform
 * @author Matthew Pocock
 */
public interface RdfEntityPickler<E> {
  /**
   * Add RDF for a Java object to a model.
   *
   * @param model   the Model to add to
   * @param e       the Java object to add
   */
  public void pickle(Model model, E e);

  public static class WithType<E> implements RdfEntityPickler<E> {
    private final String type;
    private final ResourceMaker<? super E> fromResourcer;

    public WithType(String type, ResourceMaker<? super E> fromResourcer) {
      this.type = type;
      this.fromResourcer = fromResourcer;
    }

    @Override
    public void pickle(Model model, E e) {
      model.add(fromResourcer.asResource(model, e), RDF.type, type);
    }
  }

  public static final class All<T> implements RdfEntityPickler<T> {
    private final RdfEntityPickler<? super T>[] picklers;

    public All(RdfEntityPickler<? super T>[] picklers) {
      this.picklers = picklers;
    }

    @Override
    public void pickle(Model model, T t) {
      for(RdfEntityPickler<? super T> p : picklers) {
        p.pickle(model, t);
      }
    }
  }

  public static abstract class ByProperty<E, P> implements RdfEntityPickler<E> {
    private final RdfRelationshipPickler<E, P> relationshipPickler;

    protected ByProperty(RdfRelationshipPickler<E, P> relationshipPickler) {
      this.relationshipPickler = relationshipPickler;
    }

    protected abstract P target(E entity);

    @Override
    public void pickle(Model model, E e) {
      relationshipPickler.pickle(model, e, target(e));
    }
  }
}
