package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.SBOLNamedObject;

/**
 * Pickler for {@link org.sbolstandard.core.SBOLNamedObject}.
 *
 * @author Matthew Pocock
 */
public class SBOLNamedObjectRdfEntityPickler implements RdfEntityPickler<SBOLNamedObject> {
  private final SBOLObjectRdfEntityPickler sbolObjectRdfPickler;
  private final String name;
  private final String description;
  private final String displayId;

  public SBOLNamedObjectRdfEntityPickler(SBOLObjectRdfEntityPickler sbolObjectRdfPickler, String name, String description, String displayId) {
    this.sbolObjectRdfPickler = sbolObjectRdfPickler;
    this.name = name;
    this.description = description;
    this.displayId = displayId;
  }

  @Override
  public void pickle(Model model, SBOLNamedObject sbolNamedObject) {
    sbolObjectRdfPickler.pickle(model, sbolNamedObject);

    Resource thisRes = asResource(model, sbolNamedObject);
    SBOLObjectRdfEntityPickler.addNotNullPropertyStmt(model, thisRes, name, sbolNamedObject.getName());
    SBOLObjectRdfEntityPickler.addNullablePropertyStmt(model, thisRes, description, sbolNamedObject.getDescription());
    SBOLObjectRdfEntityPickler.addNullablePropertyStmt(model, thisRes, displayId, sbolNamedObject.getDisplayId());
  }

  @Override
  public Resource asResource(Model model, SBOLNamedObject sbolNamedObject) {
    return sbolObjectRdfPickler.asResource(model, sbolNamedObject);
  }
}
