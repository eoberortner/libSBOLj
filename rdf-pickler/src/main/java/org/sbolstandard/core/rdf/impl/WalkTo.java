package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfEntityPickler;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class WalkTo<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfEntityPickler<P> entityPickler;

  public WalkTo(RdfEntityPickler<P> entityPickler) {
    this.entityPickler = entityPickler;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    entityPickler.pickle(model, to);
  }
}
