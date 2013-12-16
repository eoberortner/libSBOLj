package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.DnaSequence;

import static org.sbolstandard.core.rdf.SBOLObjectRdfEntityPickler.*;

/**
 * RDF pickler for {@link org.sbolstandard.core.DnaSequence}.
 *
 * @author Matthew Pocock
 */
public class DnaSequenceRdfEntityPickler implements RdfEntityPickler<DnaSequence> {
  private final SBOLObjectRdfEntityPickler sbolObjectRdfPickler;
  private final String nucleotides;

  public DnaSequenceRdfEntityPickler(SBOLObjectRdfEntityPickler sbolObjectRdfPickler, String nucleotides) {
    this.sbolObjectRdfPickler = sbolObjectRdfPickler;
    this.nucleotides = nucleotides;
  }

  @Override
  public void pickle(Model model, DnaSequence dnaSequence) {
    sbolObjectRdfPickler.pickle(model, dnaSequence);

    addNotNullPropertyStmt(model, asResource(model, dnaSequence), nucleotides, dnaSequence.getNucleotides());
  }

  @Override
  public Resource asResource(Model model, DnaSequence dnaSequence) {
    return sbolObjectRdfPickler.asResource(model, dnaSequence);
  }
}
