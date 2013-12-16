package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfEntityPickler;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class AllEntityPicklers<T> implements RdfEntityPickler<T> {
  private final RdfEntityPickler<? super T>[] picklers;

  public AllEntityPicklers(RdfEntityPickler<? super T>[] picklers) {
    this.picklers = picklers;
  }

  @Override
  public void pickle(Model model, T t) {
    for(RdfEntityPickler<? super T> p : picklers) {
      p.pickle(model, t);
    }
  }
}
