package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
 * Validate that an entity is not null and then pickle it using a wrapped pickler, raising a
 * {@link java.lang.NullPointerException} on nulls.
 *
 * @param <E> the entity type
 * @param <P> the propertyMaker value type
 * @author Matthew Pocock
 */
public final class IsNotNull<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfPropertyPickler<? super E, P> wrapped;

  /**
   *
   * @param wrapped   the pickler to use after null checks
   */
  public IsNotNull(RdfPropertyPickler<? super E, P> wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    if(to == null) throw new NullPointerException(
            "Can't add a null property value for property");
    wrapped.pickle(model, from, to);
  }
}
