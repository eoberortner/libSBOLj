package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.PropertyMaker;
import org.sbolstandard.core.rdf.RdfPropertyPickler;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class AnObject<E, P> implements RdfPropertyPickler<E, P> {
  private final ResourceMaker<? super E> fromResource;
  private final PropertyMaker property;
  private final ResourceMaker<? super P> toResource;

  public AnObject(ResourceMaker<? super E> fromResource, PropertyMaker property, ResourceMaker<? super P> toResource) {
    this.fromResource = fromResource;
    this.property = property;
    this.toResource = toResource;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    model.add(fromResource.asResource(model, from), property.propertyFor(model), toResource.asResource(model, to));
  }
}
