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
  /**
   * Return a resource for {@code r}.
   *
   * <p>
   *   Successive calls for equivalent {@code r} should return equivalent resources.
   * </p>
   *
   * <p>
   *   Implementations are free to cache resources. However, they should be careful not to retain a hard reference to
   *   the {@code model}.
   * </p>
   *
   * @param model   the model to contain the resource
   * @param r       the object to find the resource for
   * @return        a resource which represents {@code r}
   */
  public Resource asResource(Model model, R r);

  /**
   * An implementation of {@link org.sbolstandard.core.rdf.ResourceMaker} that uses
   * {@link org.sbolstandard.core.SBOLObject#getURI()}.
   *
   * @author Matthew Pocock
   */
  public static final class FromIdentity implements ResourceMaker<SBOLObject> {
    @Override
    public Resource asResource(Model model, SBOLObject r) {
      return model.createResource(r.getURI().toString());
    }
  }
}
