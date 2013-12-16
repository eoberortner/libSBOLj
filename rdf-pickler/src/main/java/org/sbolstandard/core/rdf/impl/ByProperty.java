package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfEntityPickler;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
* Created by nmrp3 on 16/12/13.
*/
public abstract class ByProperty<E, P> implements RdfEntityPickler<E> {
  private final RdfPropertyPickler<? super E, P> relationshipPickler;

  public ByProperty(RdfPropertyPickler<? super E, P> relationshipPickler) {
    this.relationshipPickler = relationshipPickler;
  }

  protected abstract P target(E entity);

  @Override
  public void pickle(Model model, E e) {
    relationshipPickler.pickle(model, e, target(e));
  }
}
