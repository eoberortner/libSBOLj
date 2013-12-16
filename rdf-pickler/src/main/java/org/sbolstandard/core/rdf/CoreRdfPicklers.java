package org.sbolstandard.core.rdf;

import org.sbolstandard.core.*;

import static org.sbolstandard.core.rdf.RdfPicklers.*;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * RDF picklers for the core data model.
 *
 * <p>
 *   This is intended to be used through the {@link #instance()} singleton. This will give you picklers configured
 *   against the v1 SBOL terminology. If you want to use an alternative terminology, call
 *   {@link #CoreRdfPicklers(java.util.Properties)} with your own properties containing bindings for that terminology.
 * </p>
 *
 * @author Matthew Pocock
 */
public class CoreRdfPicklers {
  private static CoreRdfPicklers instance = null;

  /**
   * Get picklers with the default configuration.
   *
   * @return  a default-valued CoreRdfPicklers instance
   * @throws java.io.IOException  if the resource used to configure this instance could not be loaded
   */
  public static CoreRdfPicklers instance() throws IOException, IntrospectionException {
    if(instance == null) {
      Properties props = new Properties();
      props.load(RdfPicklers.class.getResourceAsStream("SbolRdfPicklers.properties"));
      instance = new CoreRdfPicklers(props);
    }

    return instance;
  }

  private static String getProperty(Properties props, String propName) {
    String prop = (String) props.get(propName);
    if(prop == null) throw new IllegalArgumentException("Missing property: " + propName);
    return prop;
  }

  private static Properties propertiesFor(Properties properties, String pfx) {
    Properties result = new Properties();
    String pfxDot = pfx + ".";
    int pfxDotLen = pfxDot.length();

    for(Map.Entry<Object, Object> ent : properties.entrySet()) {
      String key = (String) ent.getKey();
      if(key.startsWith(pfxDot)) {
        result.put(key.substring(pfxDotLen), ent.getValue());
      }
    }

    return result;
  }


  private final RdfEntityPickler<SBOLNamedObject> sbolNamedObjectPickler;
  private final RdfEntityPickler<DnaSequence> dnaSequencePickler;
  private final RdfEntityPickler<DnaComponent> dnaComponentPickler;
  private final RdfEntityPickler<SequenceAnnotation> sequenceAnnotationPickler;
  private final RdfEntityPickler<Collection> collectionPickler;

  private final RdfEntityPickler<Collection> collectionComponentsPickler;
  private final RdfEntityPickler<DnaComponent> nestedDnaComponentsPickler;

  /**
   * Create a new SbolRdfPicklers instance using the supplied properties
   *
   * @param props   the Properties describing URIs associated with types and relations
   */
  public CoreRdfPicklers(Properties props) throws IntrospectionException {
    // initialization order is important here
    sbolNamedObjectPickler = mkSbolNamedObjectPickler(props);
    dnaSequencePickler = mkDnaSequencePickler(props);
    sequenceAnnotationPickler = mkSequenceAnnotationPickler(props);
    dnaComponentPickler = mkDnaComponentPickler(props);
    collectionPickler = mkCollectionRdfPickler(props);

    collectionComponentsPickler = mkCollectionComponentsPickler();
    nestedDnaComponentsPickler = mkNestedDnaComponentsPickler();
  }

  /**
   * Get the pickler for collections.
   *
   * <p>
   *   This is configured to pickle the collection but not the collection members.
   * </p>
   *
   * @return  the pickler for {@link Collection}
   */
  public RdfEntityPickler<Collection> getCollectionPickler() {
    return collectionPickler;
  }

  /**
   * Get the pickler for DNA sequences.
   *
   * @return  the pickler for {@link org.sbolstandard.core.DnaSequence}
   */
  public RdfEntityPickler<DnaSequence> getDnaSequencePickler() {
    return dnaSequencePickler;
  }

  /**
   * Get the pickler for DNA components.
   *
   * <p>
   *   This is configured to pickle the DNA component and all nested {@link org.sbolstandard.core.SequenceAnnotation}s
   *   but doesn't recursively pickle nested DNA components.
   * </p>
   *
   * @return  the pickler for {@link org.sbolstandard.core.DnaComponent} and nested
   *          {@link org.sbolstandard.core.SequenceAnnotation}s
   */
  public RdfEntityPickler<DnaComponent> getDnaComponentPickler() {
    return dnaComponentPickler;
  }

  /**
   * Get the pickler for DNA components nested within other DNA components (one level deep).
   *
   * @return the pickler for {@link org.sbolstandard.core.DnaComponent} that drills down one level into nested
   *          annotations and their components
   */
  public RdfEntityPickler<DnaComponent> getNestedDnaComponentsPickler() {
    return nestedDnaComponentsPickler;
  }

  /**
   * Get the pickler for collection components.
   *
   * <p>
   *   This is configured to pickle the components contained within a collection without pickling the collection.
   * </p>
   *
   * @return the pickler for {@link org.sbolstandard.core.Collection#getComponents()}
   */
  public RdfEntityPickler<Collection> getCollectionComponentsPickler() {
    return collectionComponentsPickler;
  }


  private RdfEntityPickler<SBOLNamedObject> mkSbolNamedObjectPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "NamedObject");

    RdfPropertyPickler<SBOLNamedObject, String> name =
            value(identity, property(getProperty(cProps, "name")));
    RdfPropertyPickler<SBOLNamedObject, String> displayId =
            value(identity, property(getProperty(cProps, "displayId")));
    RdfPropertyPickler<SBOLNamedObject, String> description =
            value(identity, property(getProperty(cProps, "description")));

    return all(
            byProperty(SBOLNamedObject.class, "name", notNull(name)),
            byProperty(SBOLNamedObject.class, "displayId", nullable(displayId)),
            byProperty(SBOLNamedObject.class, "description", nullable(description)));
  }

  private RdfEntityPickler<DnaSequence> mkDnaSequencePickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "DnaSequence");

    RdfPropertyPickler<DnaSequence, String> nucleotides =
            value(identity, property(getProperty(cProps, "nucleotides")));

    return all(
            type(getProperty(props, "DnaSequence"), identity),
            byProperty(DnaSequence.class, "nucleotides", notNull(nucleotides)));
  }

  private RdfEntityPickler<SequenceAnnotation> mkSequenceAnnotationPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "SequenceAnnotation");

    RdfPropertyPickler<SequenceAnnotation, Integer> bioStart =
            value(identity, property(getProperty(cProps, "bioStart")));
    RdfPropertyPickler<SequenceAnnotation, Integer> bioEnd =
            value(identity, property(getProperty(cProps, "bioEnd")));
    RdfPropertyPickler<SequenceAnnotation, StrandType> strand =
            value(identity, property(getProperty(cProps, "strand")));
    RdfPropertyPickler<SequenceAnnotation, DnaComponent> subComponent =
            object(identity, property(getProperty(cProps, "subComponent")), identity);
    RdfPropertyPickler<SequenceAnnotation, DnaComponent> precedes =
            object(identity, property(getProperty(cProps, "precedes")), identity);

    return all(
            type(getProperty(props, "SequenceAnnotation"), identity),
            byProperty(SequenceAnnotation.class, "bioStart", nullable(bioStart)),
            byProperty(SequenceAnnotation.class, "bioEnd", nullable(bioEnd)),
            byProperty(SequenceAnnotation.class, "strand", nullable(strand)),
            byProperty(SequenceAnnotation.class, "subComponent", notNull(subComponent)),
            byProperty(SequenceAnnotation.class, "precedes", notNull(collection(precedes))));
  }

  private RdfEntityPickler<DnaComponent> mkDnaComponentPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "DnaComponent");

    RdfPropertyPickler<DnaComponent, DnaSequence> sequence =
            object(identity, property(getProperty(cProps, "sequence")), identity);
    RdfPropertyPickler<DnaComponent, SequenceAnnotation> annotation =
            object(identity, property(getProperty(cProps, "annotation")), identity);

    return all(
            type(getProperty(props, "DnaComponent"), identity),
            byProperty(DnaComponent.class, "sequence", nullable(sequence)),
            byProperty(DnaComponent.class, "annotations", notNull(collection(all(annotation, walkTo(sequenceAnnotationPickler))))),
            sbolNamedObjectPickler);
  }

  private RdfEntityPickler<Collection> mkCollectionRdfPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "Collection");

    RdfPropertyPickler<Collection, DnaComponent> component =
            object(identity, property(getProperty(cProps, "component")), identity);

    return all(
            type(getProperty(props, "Collection"), identity),
            byProperty(Collection.class, "component", collection(notNull(component))),
            sbolNamedObjectPickler);
  }

  private RdfEntityPickler<Collection> mkCollectionComponentsPickler() throws IntrospectionException {
    RdfPropertyPickler<Collection, DnaComponent> component =
            walkTo(dnaComponentPickler);

    return byProperty(Collection.class, "component", collection(notNull(component)));
  }

  private RdfEntityPickler<DnaComponent> mkNestedDnaComponentsPickler() throws IntrospectionException {
    return byProperty(DnaComponent.class, "annotations",
            collection(notNull(walkTo(byProperty(SequenceAnnotation.class, "subComponent",
                    notNull(walkTo(dnaComponentPickler)))))));
  }
}
