package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfEntityPickler;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
 * Pickle the property value using an entity pickler.
 *
 * <p>
 *   This allows picklers to drill down into nested structures.
 * </p>
 *
 * @param <E> the entity type
 * @param <P> the propertyMaker value type
 * @author Matthew Pocock
 */
public final class WalkTo<E, P> implements RdfPropertyPickler<E, P> {
  private final RdfEntityPickler<P> entityPickler;

  /**
   *
   * @param entityPickler the pickler used to pickle {@code to} in
   *                      {@link #pickle(com.hp.hpl.jena.rdf.model.Model, Object, Object)}
   */
  public WalkTo(RdfEntityPickler<P> entityPickler) {
    this.entityPickler = entityPickler;
  }

  @Override
  public void pickle(Model model, E from, P to) {
    entityPickler.pickle(model, to);
  }
}
