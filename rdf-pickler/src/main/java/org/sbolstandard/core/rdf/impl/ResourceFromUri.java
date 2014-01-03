package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.SBOLObject;
import org.sbolstandard.core.rdf.ResourceMaker;

import java.net.URI;

/**
 * An implementation of {@link org.sbolstandard.core.rdf.ResourceMaker} that uses
 * {@link org.sbolstandard.core.SBOLObject#getURI()}.
 *
 * @author Matthew Pocock
 */
public final class ResourceFromUri implements ResourceMaker<URI> {
  @Override
  public Resource asResource(Model model, URI r) {
    return model.createResource(r.toString());
  }
}
