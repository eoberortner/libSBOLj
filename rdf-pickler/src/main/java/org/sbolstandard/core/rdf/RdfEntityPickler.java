package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Pickler for Java objects to RDF entities.
 *
 * @param  <E> the Java type to transform
 * @author Matthew Pocock
 */
public interface RdfEntityPickler<E> {
  /**
   * Add RDF for a Java object to a model.
   *
   * @param model   the Model to add to
   * @param e       the Java object to add
   */
  public void pickle(Model model, E e);

}
