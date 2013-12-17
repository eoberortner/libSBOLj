package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.PropertyMaker;
import org.sbolstandard.core.rdf.RdfPropertyPickler;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
 * Pickle as an RDF triple where the property value is represented as a literal using a String.
 *
 * @param <E> the entity type
 * @param <P> the property value type
 * @author Matthew Pocock
 */
public abstract class AValue<E, P> implements RdfPropertyPickler<E, P> {
  private final ResourceMaker<? super E> fromResource;
  private final PropertyMaker propertyMaker;

  /**
   *
   * @param fromResource    resource maker for the entity
   * @param propertyMaker   property maker
   */
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
