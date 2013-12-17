package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.RDF;
import org.sbolstandard.core.rdf.RdfEntityPickler;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
 * Pickle an entity by asserting an RDF type.
 *
 * @param <E> the entity type
 * @author Matthew Pocock
 */
public class WithType<E> implements RdfEntityPickler<E> {
  private final String type;
  private final ResourceMaker<? super E> fromResourcer;

  /**
   *
   * @param fromResourcer   resource maker for the entity
   * @param type            the RDF {@code type}
   */
  public WithType(ResourceMaker<? super E> fromResourcer, String type) {
    this.type = type;
    this.fromResourcer = fromResourcer;
  }

  @Override
  public void pickle(Model model, E e) {
    model.add(fromResourcer.asResource(model, e), RDF.type, type);
  }
}
