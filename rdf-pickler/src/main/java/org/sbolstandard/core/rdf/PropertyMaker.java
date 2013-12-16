package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * Created by nmrp3 on 16/12/13.
 */
public interface PropertyMaker {
  public Property propertyFor(Model model);

  public static final class ByName implements PropertyMaker {
    private final String name;

    public ByName(String name) {
      this.name = name;
    }

    @Override
    public Property propertyFor(Model model) {
      return model.createProperty(name);
    }
  }
}
