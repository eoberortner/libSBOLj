package org.sbolstandard.core.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import org.sbolstandard.core.*;

import static org.sbolstandard.core.rdf.RdfPicklers.*;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Writer;
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

  private final String baseURI;

  // URIs for resources
  private final String dnaSequence;
  private final String sequenceAnnotation;
  private final String dnaComponent;
  private final String collection;

  // picklers
  private final RdfEntityPickler<SBOLNamedObject> sbolNamedObjectPickler;
  private final RdfEntityPickler<DnaSequence> dnaSequencePickler;
  private final RdfEntityPickler<DnaComponent> dnaComponentPickler;
  private final RdfEntityPickler<SequenceAnnotation> sequenceAnnotationPickler;
  private final RdfEntityPickler<Collection> collectionPickler;

  // exotic picklers
  private final RdfEntityPickler<Collection> collectionComponentsPickler;
  private final RdfEntityPickler<DnaComponent> nestedDnaComponentsPickler;

  /**
   * Create a new SbolRdfPicklers instance using the supplied properties
   *
   * @param props   the Properties describing URIs associated with types and relations
   */
  public CoreRdfPicklers(Properties props) throws IntrospectionException {
    // initialization order is important here
    baseURI = getProperty(props, "baseURI");
    dnaSequence = getProperty(props, "DnaSequence");
    sequenceAnnotation = getProperty(props, "SequenceAnnotation");
    dnaComponent = getProperty(props, "DnaComponent");
    collection = getProperty(props, "Collection");

    sbolNamedObjectPickler = mkSbolNamedObjectPickler(props);
    dnaSequencePickler = mkDnaSequencePickler(props);
    sequenceAnnotationPickler = mkSequenceAnnotationPickler(props);
    dnaComponentPickler = mkDnaComponentPickler(props);
    collectionPickler = mkCollectionRdfPickler(props);

    collectionComponentsPickler = mkCollectionComponentsPickler();
    nestedDnaComponentsPickler = mkNestedDnaComponentsPickler();
  }

  public void pickle(final Model model, SBOLDocument document) {
    for(SBOLObject o : document.getContents()) {
      o.accept(new SBOLVisitor<RuntimeException>() {
        @Override
        public void visit(SBOLDocument doc) throws RuntimeException {
          // skip
        }

        @Override
        public void visit(Collection coll) throws RuntimeException {
          getCollectionPickler().pickle(model, coll);
        }

        @Override
        public void visit(DnaComponent component) throws RuntimeException {
          getDnaComponentPickler().pickle(model, component);
        }

        @Override
        public void visit(DnaSequence sequence) throws RuntimeException {
          getDnaSequencePickler().pickle(model, sequence);
        }

        @Override
        public void visit(SequenceAnnotation annotation) throws RuntimeException {
          // skip - there should be none of these at top-level
        }
      });
    }
  }

  /**
   * Get the base URI for the terminology used by these picklers.
   *
   * @return  the base URI
   */
  public String getBaseUri() {
    return baseURI;
  }

  /**
   * Resource name for the DnaSequence type.
   *
   * @return  the DnaSequence resource name
   */
  public String getDnaSequence() {
    return dnaSequence;
  }

  /**
   * Resoure name for the SequenceAnnotation type.
   *
   * @return  the SequenceAnnotation resource name
   */
  public String getSequenceAnnotation() {
    return sequenceAnnotation;
  }

  /**
   * Resource name for the DnaComponent type.
   *
   * @return  the DnaComponent resource name
   */
  public String getDnaComponent() {
    return dnaComponent;
  }

  /**
   * Resource name for the Collection type.
   *
   * @return  the Collection resource name
   */
  public String getCollection() {
    return collection;
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
            type(dnaSequence, identity),
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
            type(sequenceAnnotation, identity),
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
            type(dnaComponent, identity),
            byProperty(DnaComponent.class, "sequence", nullable(sequence)),
            byProperty(DnaComponent.class, "annotations", notNull(collection(all(annotation, walkTo(sequenceAnnotationPickler))))),
            sbolNamedObjectPickler);
  }

  private RdfEntityPickler<Collection> mkCollectionRdfPickler(Properties props) throws IntrospectionException {
    Properties cProps = propertiesFor(props, "Collection");

    RdfPropertyPickler<Collection, DnaComponent> component =
            object(identity, property(getProperty(cProps, "component")), identity);

    return all(
            type(collection, identity),
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

  public static interface IO {
    public void write(SBOLDocument document, Writer rdfOut) throws IOException;
    public void write(Model model, Writer rdfOut) throws IOException;
  }

  public IO getIO() {
    String format = "RDF/XML-ABBREV";
    return getIO(format, getDnaComponent(), getDnaSequence(), getCollection());
  }

  public IO getIO(final String format, final String ... topLevel) {
    return new IO() {
      public void write(Model model, Writer rdfOut) throws IOException
      {
        RDFWriter writer = model.getWriter(format);
        writer.setProperty("tab","3");

        Resource[] topLevelResources = new Resource[topLevel.length];
        for(int i = 0; i < topLevel.length; i++) {
          topLevelResources[i] = model.createResource(topLevel[i]);
        }

        writer.setProperty("prettyTypes", topLevelResources);
        writer.write(model, rdfOut, getBaseUri());
      }

      @Override
      public void write(SBOLDocument document, Writer rdfOut) throws IOException {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("sbol", getBaseUri());
        pickle(model, document);
        write(model, rdfOut);
      }
    };
  }
}
