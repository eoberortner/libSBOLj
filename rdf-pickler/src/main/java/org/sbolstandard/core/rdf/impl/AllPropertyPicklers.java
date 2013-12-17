package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
 * Apply an array of picklers.
 *
 * @param <E> the entity type
 * @param <P> the property value type
 * @author Matthew Pocock
 */
public final class AllPropertyPicklers<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfPropertyPickler<? super E, P>[] picklers;

  /**
   *
   * @param picklers  the array of picklers to apply
   */
  public AllPropertyPicklers(RdfPropertyPickler<? super E, P>[] picklers) {
    this.picklers = picklers;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    for(RdfPropertyPickler<? super E, P> p : picklers) {
      p.pickle(model, from, to);
    }
  }
}
