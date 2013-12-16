package org.sbolstandard.core.rdf;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Helpful utility for presenting a collection of configured picklers.
 *
 * @author Matthew Pocock
 */
public class SbolRdfPicklers {
  private final CollectionRdfPickler collectionRdfPickler;
  private final DnaComponentRdfPickler dnaComponentRdfPickler;
  private final DnaSequenceRdfPickler dnaSequenceRdfPickler;
  private final SequenceAnnotationRdfPickler sequenceAnnotationRdfPickler;

  /**
   * Get an SbolRdfPicklers with the default configuration.
   *
   * @return  a default-valued SbolRdfPicklers instance
   * @throws IOException  if the resource used to configure this instance could not be loaded
   */
  public static SbolRdfPicklers instance() throws IOException {
    Properties props = new Properties();
    props.load(SbolRdfPicklers.class.getResourceAsStream("SbolRdfPicklers.properties"));
    return new SbolRdfPicklers(props);
  }

  /**
   * Create a new SbolRdfPicklers instance using the supplied properties
   *
   * @param props   the Properties describing URIs associated with types and relations
   */
  public SbolRdfPicklers(Properties props) {
    collectionRdfPickler = configureCollectionRdfPickler(props);
    dnaComponentRdfPickler = configureDnaComponentRdfPickler(props);
    dnaSequenceRdfPickler = configureDnaSequenceRdfPickler(props);
    sequenceAnnotationRdfPickler = configureSequenceAnnotationRdfPickler(props);
  }

  public CollectionRdfPickler getCollectionRdfPickler() {
    return collectionRdfPickler;
  }

  public DnaComponentRdfPickler getDnaComponentRdfPickler() {
    return dnaComponentRdfPickler;
  }

  public DnaSequenceRdfPickler getDnaSequenceRdfPickler() {
    return dnaSequenceRdfPickler;
  }

  public SequenceAnnotationRdfPickler getSequenceAnnotationRdfPickler() {
    return sequenceAnnotationRdfPickler;
  }

  private CollectionRdfPickler configureCollectionRdfPickler(Properties props) {
    Properties cProps = propertiesFor(props, "Collection");
    SBOLObjectRdfPickler sbolObjectRdfPickler = configureSBOLObjectRdfPickler(cProps);
    SBOLNamedObjectRdfPickler namedObjectRdfPickler = configureSBOLNamedObjectRdfPickler(props, sbolObjectRdfPickler);

    return new CollectionRdfPickler(namedObjectRdfPickler, getProperty(cProps, "component")) {
      @Override
      public DnaComponentRdfPickler getDnaComponentRdfPickler() {
        return dnaComponentRdfPickler;
      }
    };
  }

  private DnaComponentRdfPickler configureDnaComponentRdfPickler(Properties props) {
    Properties dcProps = propertiesFor(props, "DnaComponent");

    return new DnaComponentRdfPickler(configureSBOLNamedObjectRdfPickler(dcProps, configureSBOLObjectRdfPickler(dcProps)),
            getProperty(dcProps, "annotation"),
            getProperty(dcProps, "dnaSequence")) {
      @Override
      public DnaSequenceRdfPickler getDnaSequenceRdfPickler() {
        return dnaSequenceRdfPickler;
      }

      @Override
      public SequenceAnnotationRdfPickler getSequenceAnnotationRdfPickler() {
        return sequenceAnnotationRdfPickler;
      }
    };
  }

  private DnaSequenceRdfPickler configureDnaSequenceRdfPickler(Properties props) {
    Properties dsProps = propertiesFor(props, "DnaSequence");

    return new DnaSequenceRdfPickler(
            configureSBOLObjectRdfPickler(dsProps),
            getProperty(dsProps, "nucleotides"));
  }

  private SBOLNamedObjectRdfPickler configureSBOLNamedObjectRdfPickler(Properties props, SBOLObjectRdfPickler sbolObjectRdfPickler) {
    return new SBOLNamedObjectRdfPickler(sbolObjectRdfPickler,
            getProperty(props, "name"), getProperty(props, "description"), getProperty(props, "displayId"));
  }

  private SBOLObjectRdfPickler configureSBOLObjectRdfPickler(Properties props) {
    return new SBOLObjectRdfPickler(getProperty(props, "type"));
  }

  private SequenceAnnotationRdfPickler configureSequenceAnnotationRdfPickler(Properties props) {
    Properties saProps = propertiesFor(props, "SequenceAnnotation");

    SBOLObjectRdfPickler sbolObjectRdfPickler = configureSBOLObjectRdfPickler(saProps);

    return new SequenceAnnotationRdfPickler(sbolObjectRdfPickler,
            getProperty(saProps, "bioStart"),
            getProperty(saProps, "bioEnd"),
            getProperty(saProps, "strand"),
            getProperty(saProps, "precedes"),
            getProperty(saProps, "subComponent")) {
      @Override
      public DnaComponentRdfPickler getDnaComponentRdfPickler() {
        return dnaComponentRdfPickler;
      }
    };
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
}
