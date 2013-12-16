package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.RDF;
import org.sbolstandard.core.rdf.RdfEntityPickler;
import org.sbolstandard.core.rdf.ResourceMaker;

/**
* Created by nmrp3 on 16/12/13.
*/
public class WithType<E> implements RdfEntityPickler<E> {
  private final String type;
  private final ResourceMaker<? super E> fromResourcer;

  public WithType(String type, ResourceMaker<? super E> fromResourcer) {
    this.type = type;
    this.fromResourcer = fromResourcer;
  }

  @Override
  public void pickle(Model model, E e) {
    model.add(fromResourcer.asResource(model, e), RDF.type, type);
  }
}
