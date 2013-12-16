package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.Collection;

/**
 * RDF pickler for {@link org.sbolstandard.core.Collection}.
 *
 * @author Matthew Pocock
 */
public abstract class CollectionRdfEntityPickler implements RdfEntityPickler<Collection> {
  private final SBOLNamedObjectRdfEntityPickler sbolNamedObjectRdfPickler;
  private final String component;

  public CollectionRdfEntityPickler(SBOLNamedObjectRdfEntityPickler sbolNamedObjectRdfPickler, String component) {
    this.sbolNamedObjectRdfPickler = sbolNamedObjectRdfPickler;
    this.component = component;
  }

  @Override
  public void pickle(Model model, Collection collection) {
    sbolNamedObjectRdfPickler.pickle(model, collection);

    Resource thisRes = asResource(model, collection);
    SBOLObjectRdfEntityPickler.addAll(model, getDnaComponentRdfPickler(), thisRes, component, collection.getComponents());
  }

  @Override
  public Resource asResource(Model model, Collection collection) {
    return sbolNamedObjectRdfPickler.asResource(model, collection);
  }

  /**
   * Get the pickler to be used for individual component DnaComponents.
   *
   * @return the pickler for components
   */
  public abstract DnaComponentRdfEntityPickler getDnaComponentRdfPickler();
}
