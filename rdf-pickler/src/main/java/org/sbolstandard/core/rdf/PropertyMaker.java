package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * Strategy for making a stable property.
 *
 * <p>
 *   Each instance of this class is responsible for a single property which is potentially made in multiple models.
 *   It will typically store the name or URI of the property as state and then mint a new
 *   {@link com.hp.hpl.jena.rdf.model.Property} for each {@link com.hp.hpl.jena.rdf.model.Model}.
 * </p>
 *
 * @author Matthew Pocock
 */
public interface PropertyMaker {
  /**
   * Return a property.
   *
   * @param model   the model that will contain the property
   * @return        the property in {@code model}
   */
  public Property propertyFor(Model model);

}
