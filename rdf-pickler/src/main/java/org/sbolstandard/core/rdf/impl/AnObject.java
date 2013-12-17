package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.PropertyMaker;
import org.sbolstandard.core.rdf.RdfPropertyPickler;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
 * Pickle as an RDF triple where the propertyMaker value is represented as a resource.
 *
 * @param <E> the entity type
 * @param <P> the propertyMaker value type
 * @author Matthew Pocock
 */
public final class AnObject<E, P> implements RdfPropertyPickler<E, P> {
  private final ResourceMaker<? super E> fromResource;
  private final PropertyMaker propertyMaker;
  private final ResourceMaker<? super P> toResource;

  /**
   *
   * @param fromResource    resource maker for the entity
   * @param propertyMaker   property maker
   * @param toResource      resource maker for the property value
   */
  public AnObject(ResourceMaker<? super E> fromResource, PropertyMaker propertyMaker, ResourceMaker<? super P> toResource) {
    this.fromResource = fromResource;
    this.propertyMaker = propertyMaker;
    this.toResource = toResource;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    model.add(fromResource.asResource(model, from), propertyMaker.propertyFor(model), toResource.asResource(model, to));
  }
}
