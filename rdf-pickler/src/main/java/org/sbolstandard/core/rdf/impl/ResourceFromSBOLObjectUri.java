package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.SBOLObject;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
 * An implementation of {@link org.sbolstandard.core.rdf.ResourceMaker} that uses
 * {@link org.sbolstandard.core.SBOLObject#getURI()}.
 *
 * @author Matthew Pocock
 */
public final class ResourceFromSBOLObjectUri implements ResourceMaker<SBOLObject> {
  @Override
  public Resource asResource(Model model, SBOLObject r) {
    return model.createResource(r.getURI().toString());
  }
}
