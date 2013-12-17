package org.sbolstandard.core.rdf.impl;

import com.hp.hpl.jena.rdf.model.Model;
import org.sbolstandard.core.rdf.RdfEntityPickler;
import org.sbolstandard.core.rdf.RdfPropertyPickler;

/**
 * Pickle an entity by pickling a property.
 *
 * <p>
 *   To bind this to specific properties, subclass and implement {@link #target(Object)}.
 * </p>
 *
 * @param <E> the entity type
 * @param <P> the propertyMaker value type
 * @author Matthew Pocock
 */
public abstract class ByProperty<E, P> implements RdfEntityPickler<E> {
  private final RdfPropertyPickler<? super E, P> relationshipPickler;

  /**
   *
   * @param propertyPickler   the pickler used to pickle the property
   */
  public ByProperty(RdfPropertyPickler<? super E, P> propertyPickler) {
    this.relationshipPickler = propertyPickler;
  }

  protected abstract P target(E entity);

  @Override
  public void pickle(Model model, E e) {
    relationshipPickler.pickle(model, e, target(e));
  }
}
