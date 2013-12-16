package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Pickler for Java properties to RDF relationships.
 *
 * @author Matthew Pocock
 */
public interface RdfPropertyPickler<E, P> {
  /**
   * Add RDF for a java relationship to a model.
   *
   * @param model   the Model to add to
   * @param from    the Java object that is the source of the property
   * @param to      the Java object that is the target of the property
   */
  public void pickle(Model model, E from, P to);

}
