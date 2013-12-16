package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.SBOLObject;

/**
 * Strategy for generating stable resources from Java instances.
 *
 * @author Matthew Pocock
 */
public interface ResourceMaker<R> {
  public Resource asResource(Model model, R r);

  public static final class FromIdentity implements ResourceMaker<SBOLObject> {
    @Override
    public Resource asResource(Model model, SBOLObject r) {
      return model.createResource(r.getURI().toString());
    }
  }
}
