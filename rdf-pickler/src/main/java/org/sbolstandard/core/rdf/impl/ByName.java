package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import org.sbolstandard.core.rdf.PropertyMaker;

/**
* Created by nmrp3 on 16/12/13.
*/
public final class ByName implements PropertyMaker {
  private final String name;

  public ByName(String name) {
    this.name = name;
  }

  @Override
  public Property propertyFor(Model model) {
    return model.createProperty(name);
  }
}
