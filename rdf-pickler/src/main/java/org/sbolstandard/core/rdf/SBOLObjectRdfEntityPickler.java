package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import org.sbolstandard.core.SBOLObject;

import java.util.Collection;

/**
 * RDF pickler for {@link org.sbolstandard.core.SBOLObject}.
 * <p>
 *   This pickler
 * </p>
 */
public class SBOLObjectRdfEntityPickler implements RdfEntityPickler<SBOLObject> {
  private final String type;

  public SBOLObjectRdfEntityPickler(String type) {
    this.type = type;
  }

  @Override
  public void pickle(Model model, SBOLObject sbolObject) {
    addNotNullPropertyStmt(model, asResource(model, sbolObject), RDF.type, type);
  }

  public Resource asResource(Model model, SBOLObject sbolObject) {
    // todo: consider caching this, but not if it needs caching per model
    return model.createResource(sbolObject.getURI().toString());
  }
  
  public static <P> void addAll(Model model, RdfEntityPickler<P> pickler, Resource subject, String predicated, Collection<P> objects) {
    for(P p : objects) {
      addNotNullPropertyStmt(model, pickler, subject, predicated, p);
    }
  }

  public static void addNullablePropertyStmt(Model model, Resource subject, String predicate, Integer object) {
    if(object != null) {
      model.add(subject, model.createProperty(predicate), object.toString());
    }
  }

  public static <E extends Enum<E>> void addNullablePropertyStmt(Model model, Resource subject, String predicate, E object) {
    if(object != null) {
      model.add(subject, model.createProperty(predicate), object.toString());
    }
  }

  public static void addNullablePropertyStmt(Model model, Resource subject, String predicate, String object) {
    if(object != null) {
      model.add(subject, model.createProperty(predicate), object);
    }
  }

  public static <P> void addNullablePropertyStmt(Model model, RdfEntityPickler<P> pickler, Resource subject, String predicate, P object) {
    if(object != null) {
      model.add(subject, model.createProperty(predicate), pickler.asResource(model, object));
    }
  }

  public static void addNotNullPropertyStmt(Model model, Resource subject, String property, String object) {
    if(object == null) throw new NullPointerException("Can't add a null property value for property: " + property);
    model.add(subject, model.createProperty(property), object);
  }

  public static <P> void addNotNullPropertyStmt(Model model, RdfEntityPickler<P> pickler, Resource subject, String property, P object) {
    if(object == null) throw new NullPointerException("Can't add a null property value for property: " + property);
    model.add(subject, model.createProperty(property), pickler.asResource(model, object));
  }

  public static void addNotNullPropertyStmt(Model model, Resource subject, Property property, String object) {
    if(object == null) throw new NullPointerException("Can't add a null property value for property: " + property);
    model.add(subject, property, object);
  }
}
