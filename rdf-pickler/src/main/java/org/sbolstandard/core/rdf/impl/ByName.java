package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import org.sbolstandard.core.rdf.PropertyMaker;

/**
 * A property maker that uses the same name string over and over.
 *
 * @author Matthew Pocock
 */
public final class ByName implements PropertyMaker {
  private final String name;

  /**
   *
   * @param name  the name (uri) of the property that will be made
   */
  public ByName(String name) {
    this.name = name;
  }

  @Override
  public Property propertyFor(Model model) {
    return model.createProperty(name);
  }
}
