package org.sbolstandard.core.rdf;

import org.sbolstandard.core.*;
import static org.sbolstandard.core.rdf.SbolRdfPicklers.*;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nmrp3 on 16/12/13.
 */
public class CoreRdfPicklers {
  private static CoreRdfPicklers instance = null;

  /**
   * Get an SbolRdfPicklers with the default configuration.
   *
   * @return  a default-valued SbolRdfPicklers instance
   * @throws java.io.IOException  if the resource used to configure this instance could not be loaded
   */
  public static CoreRdfPicklers instance() throws IOException, IntrospectionException {
    if(instance == null) {
      Properties props = new Properties();
      props.load(SbolRdfPicklers.class.getResourceAsStream("SbolRdfPicklers.properties"));
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
  }

  public RdfEntityPickler<Collection> getCollectionPickler() {
    return collectionPickler;
  }

  public RdfEntityPickler<DnaComponent> getDnaComponentPickler() {
    return dnaComponentPickler;
  }

  public RdfEntityPickler<DnaSequence> getDnaSequencePickler() {
    return dnaSequencePickler;
  }

  public RdfEntityPickler<SequenceAnnotation> getSequenceAnnotationPickler() {
    return sequenceAnnotationPickler;
  }


  private RdfEntityPickler<SBOLNamedObject> mkSbolNamedObjectPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "NamedObject");

    RdfRelationshipPickler<SBOLNamedObject, String> name =
            value(identity, property(getProperty(cProps, "name")));
    RdfRelationshipPickler<SBOLNamedObject, String> displayId =
            value(identity, property(getProperty(cProps, "displayId")));
    RdfRelationshipPickler<SBOLNamedObject, String> description =
            value(identity, property(getProperty(cProps, "description")));

    return all(
            byProperty(SBOLNamedObject.class, "name", notNull(name)),
            byProperty(SBOLNamedObject.class, "displayId", nullable(displayId)),
            byProperty(SBOLNamedObject.class, "description", nullable(description)));
  }

  private RdfEntityPickler<DnaSequence> mkDnaSequencePickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "DnaSequence");

    RdfRelationshipPickler<DnaSequence, String> nucleotides =
            value(identity, property(getProperty(cProps, "nucleotides")));

    return all(
            type(getProperty(props, "DnaSequence"), identity),
            byProperty(DnaSequence.class, "nucleotides", notNull(nucleotides)));
  }

  private RdfEntityPickler<SequenceAnnotation> mkSequenceAnnotationPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "SequenceAnnotation");

    RdfRelationshipPickler<SequenceAnnotation, Integer> bioStart =
            value(identity, property(getProperty(cProps, "bioStart")));
    RdfRelationshipPickler<SequenceAnnotation, Integer> bioEnd =
            value(identity, property(getProperty(cProps, "bioEnd")));
    RdfRelationshipPickler<SequenceAnnotation, StrandType> strand =
            value(identity, property(getProperty(cProps, "strand")));
    RdfRelationshipPickler<SequenceAnnotation, DnaComponent> subComponent =
            object(identity, property(getProperty(cProps, "subComponent")), identity);
    RdfRelationshipPickler<SequenceAnnotation, DnaComponent> precedes =
            object(identity, property(getProperty(cProps, "precedes")), identity);

    return all(
            type(getProperty(props, "SequenceAnnotation"), identity),
            byProperty(SequenceAnnotation.class, "bioStart", nullable(bioStart)),
            byProperty(SequenceAnnotation.class, "bioEnd", nullable(bioEnd)),
            byProperty(SequenceAnnotation.class, "strand", nullable(strand)),
            byProperty(SequenceAnnotation.class, "subComponent", notNull(collection(subComponent))),
            byProperty(SequenceAnnotation.class, "precedes", notNull(collection(precedes))));
  }

  private RdfEntityPickler<DnaComponent> mkDnaComponentPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "DnaComponent");

    RdfRelationshipPickler<DnaComponent, DnaSequence> sequence =
            object(identity, property(getProperty(cProps, "sequence")), identity);
    RdfRelationshipPickler<DnaComponent, SequenceAnnotation> annotation =
            object(identity, property(getProperty(cProps, "annotation")), identity);

    return all(
            type(getProperty(props, "DnaComponent"), identity),
            byProperty(DnaComponent.class, "sequence", nullable(sequence)),
            byProperty(DnaComponent.class, "annotation", notNull(collection(all(annotation, walkTo(sequenceAnnotationPickler))))),
            sbolNamedObjectPickler);
  }

  private RdfEntityPickler<Collection> mkCollectionRdfPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "Collection");

    RdfRelationshipPickler<Collection, DnaComponent> component =
            object(identity, property(getProperty(cProps, "component")), identity);

    return all(
            type(getProperty(props, "Collection"), identity),
            byProperty(Collection.class, "component", collection(notNull(component))),
            sbolNamedObjectPickler);
  }
}
