package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * API to take some of the pain out of transforming Java objectsRDF representations.
 *
 * @param  <T> the Java type to transform
 * @author Matthew Pocock
 */
public interface RdfEntityPickler<T> {
  /**
   * Add RDF for a Java object to a model.
   *
   * @param model   the Model to add to
   * @param t       the Java object to add
   */
  public void pickle(Model model, T t);

  /**
   * Return the resource identifying this Java object.
   *
   * @param model   the Model to contain the resource
   * @param t       the Java object that the resource is for
   * @return        a Resource which identifies the object t
   */
  public Resource asResource(Model model, T t);

}
