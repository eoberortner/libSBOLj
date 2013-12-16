package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.PropertyMaker;
import org.sbolstandard.core.rdf.RdfPropertyPickler;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
* Created by nmrp3 on 16/12/13.
*/
public abstract class AValue<E, P> implements RdfPropertyPickler<E, P> {
  private final ResourceMaker<? super E> fromResource;
  private final PropertyMaker propertyMaker;

  public AValue(ResourceMaker<? super E> fromResource, PropertyMaker propertyMaker) {
    this.fromResource = fromResource;
    this.propertyMaker = propertyMaker;
  }

  public abstract String toAsString(P to);

  @Override
  public void pickle(Model model, E from, P to) {
    model.add(fromResource.asResource(model, from), propertyMaker.propertyFor(model), toAsString(to));
  }
}
