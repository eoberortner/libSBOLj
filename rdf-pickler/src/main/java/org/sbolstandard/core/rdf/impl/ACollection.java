package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

import java.util.Collection;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class ACollection<E, P> implements RdfPropertyPickler<E, Collection<P>> {
  private final RdfPropertyPickler<E, P> wrapped;

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
