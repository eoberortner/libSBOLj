package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class AllPropertyPicklers<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfPropertyPickler<? super E, P>[] picklers;

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
