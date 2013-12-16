package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class IsNotNull<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfPropertyPickler<? super E, P> wrapped;

  public IsNotNull(RdfPropertyPickler<? super E, P> wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    if(to == null) throw new NullPointerException(
            "Can't add a null property value for property");
    wrapped.pickle(model, from, to);
  }
}
