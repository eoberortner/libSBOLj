package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class IsNullable<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfPropertyPickler<E, P> wrapped;

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
