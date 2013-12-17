package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

import java.util.Collection;

/**
 * Pickler for a collection-valued property that pickles one property for each member of the collection.
 *
 * @param <E> the entity type
 * @param <P> the collection element type
 * @author Matthew Pocock
 */
public final class ACollection<E, P> implements RdfPropertyPickler<E, Collection<P>> {
  private final RdfPropertyPickler<E, P> wrapped;

  /**
   *
   * @param wrapped the pickler for each element of the collection
   */
  public ACollection(RdfPropertyPickler<E, P> wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void pickle(Model model, E from, Collection<P> to) {
    for(P p: to) {
      wrapped.pickle(model, from, p);
    }
  }
}
