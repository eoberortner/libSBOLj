package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.DnaComponent;

import static org.sbolstandard.core.rdf.SBOLObjectRdfEntityPickler.*;

/**
 * RDF pickler for {@link org.sbolstandard.core.DnaComponent}.
 * 
 * @author Matthew Pocock
 */
public abstract class DnaComponentRdfEntityPickler implements RdfEntityPickler<DnaComponent> {
  private final SBOLNamedObjectRdfEntityPickler sbolNamedObjectRdfPickler;
  private final String annotation;
  private final String dnaSequence;

  public DnaComponentRdfEntityPickler(SBOLNamedObjectRdfEntityPickler sbolNamedObjectRdfPickler, String annotation, String dnaSequence) {
    this.sbolNamedObjectRdfPickler = sbolNamedObjectRdfPickler;
    this.annotation = annotation;
    this.dnaSequence = dnaSequence;
  }

  @Override
  public void pickle(Model model, DnaComponent dnaComponent) {
    sbolNamedObjectRdfPickler.pickle(model, dnaComponent);
    
    Resource thisRes = asResource(model, dnaComponent);
    
    addNullablePropertyStmt(model, getDnaSequenceRdfPickler(), thisRes, dnaSequence, dnaComponent.getDnaSequence());
    addAll(model, getSequenceAnnotationRdfPickler(), thisRes, annotation, dnaComponent.getAnnotations());
  }

  @Override
  public Resource asResource(Model model, DnaComponent dnaComponent) {
    return sbolNamedObjectRdfPickler.asResource(model, dnaComponent);
  }

  /**
   * Get the pickler for the associated DnaSequence.
   *
   * @return the pickler for dnaSequence
   */
  public abstract DnaSequenceRdfEntityPickler getDnaSequenceRdfPickler();

  /**
   * Get the pickler for the associated SequenceAnnotations.
   *
   * @return the annotations pickler
   */
  public abstract SequenceAnnotationRdfEntityPickler getSequenceAnnotationRdfPickler();
}
