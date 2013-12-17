package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfEntityPickler;

/**
 * Apply an array of picklers.
 *
 * @param <E> the entity type
 * @author Matthew Pocock
 */
public final class AllEntityPicklers<E> implements RdfEntityPickler<E> {
  private final RdfEntityPickler<? super E>[] picklers;

  /**
   *
   * @param picklers  the array of picklers to apply
   */
  public AllEntityPicklers(RdfEntityPickler<? super E>[] picklers) {
    this.picklers = picklers;
  }

  @Override
  public void pickle(Model model, E e) {
    for(RdfEntityPickler<? super E> p : picklers) {
      p.pickle(model, e);
    }
  }
}
