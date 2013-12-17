package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
 * Pickle entities using a wrapped pickler, doing nothing for nulls.
 *
 * @param <E> the entity type
 * @param <P> the propertyMaker value type
 * @author Matthew Pocock
 */
public final class IsNullable<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfPropertyPickler<E, P> wrapped;

  /**
   *
   * @param wrapped   the pickler to use after null checks
   */
  public IsNullable(RdfPropertyPickler<E, P> wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    if(to != null) {
      wrapped.pickle(model, from, to);
    }
  }
}
