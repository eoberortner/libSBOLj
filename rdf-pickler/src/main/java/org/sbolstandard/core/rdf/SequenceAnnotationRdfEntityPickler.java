package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.SequenceAnnotation;

/**
 * RDF pickler for {@link org.sbolstandard.core.SequenceAnnotation}.
 *
 * @author Matthew Pocock
 */
public abstract class SequenceAnnotationRdfEntityPickler implements RdfEntityPickler<SequenceAnnotation> {
  private final SBOLObjectRdfEntityPickler sbolObjectRdfPickler;
  private final String bioStart;
  private final String bioEnd;
  private final String strand;
  private final String precedes;
  private final String subComponent;

  public SequenceAnnotationRdfEntityPickler(SBOLObjectRdfEntityPickler sbolObjectRdfPickler, String bioStart, String bioEnd, String strand, String precedes, String subComponent) {
    this.sbolObjectRdfPickler = sbolObjectRdfPickler;
    this.bioStart = bioStart;
    this.bioEnd = bioEnd;
    this.strand = strand;
    this.precedes = precedes;
    this.subComponent = subComponent;
  }

  @Override
  public void pickle(Model model, SequenceAnnotation sequenceAnnotation) {
    sbolObjectRdfPickler.pickle(model, sequenceAnnotation);

    Resource thisRes = asResource(model, sequenceAnnotation);

    SBOLObjectRdfEntityPickler.addNullablePropertyStmt(model, thisRes, bioStart, sequenceAnnotation.getBioStart());
    SBOLObjectRdfEntityPickler.addNullablePropertyStmt(model, thisRes, bioEnd, sequenceAnnotation.getBioEnd());
    SBOLObjectRdfEntityPickler.addNullablePropertyStmt(model, thisRes, strand, sequenceAnnotation.getStrand());
    SBOLObjectRdfEntityPickler.addNotNullPropertyStmt(model, getDnaComponentRdfPickler(), thisRes, subComponent, sequenceAnnotation.getSubComponent());
    SBOLObjectRdfEntityPickler.addAll(model, this, thisRes, precedes, sequenceAnnotation.getPrecedes());
  }

  @Override
  public Resource asResource(Model model, SequenceAnnotation sequenceAnnotation) {
    return sbolObjectRdfPickler.asResource(model, sequenceAnnotation);
  }

  public abstract DnaComponentRdfEntityPickler getDnaComponentRdfPickler();
}
