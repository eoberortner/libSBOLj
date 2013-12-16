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
  private final CollectionRdfEntityPickler collectionRdfPickler;
  private final DnaComponentRdfEntityPickler dnaComponentRdfPickler;
  private final DnaSequenceRdfEntityPickler dnaSequenceRdfPickler;
  private final SequenceAnnotationRdfEntityPickler sequenceAnnotationRdfPickler;

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

  public CollectionRdfEntityPickler getCollectionRdfPickler() {
    return collectionRdfPickler;
  }

  public DnaComponentRdfEntityPickler getDnaComponentRdfPickler() {
    return dnaComponentRdfPickler;
  }

  public DnaSequenceRdfEntityPickler getDnaSequenceRdfPickler() {
    return dnaSequenceRdfPickler;
  }

  public SequenceAnnotationRdfEntityPickler getSequenceAnnotationRdfPickler() {
    return sequenceAnnotationRdfPickler;
  }

  private CollectionRdfEntityPickler configureCollectionRdfPickler(Properties props) {
    Properties cProps = propertiesFor(props, "Collection");
    SBOLObjectRdfEntityPickler sbolObjectRdfPickler = configureSBOLObjectRdfPickler(cProps);
    SBOLNamedObjectRdfEntityPickler namedObjectRdfPickler = configureSBOLNamedObjectRdfPickler(props, sbolObjectRdfPickler);

    return new CollectionRdfEntityPickler(namedObjectRdfPickler, getProperty(cProps, "component")) {
      @Override
      public DnaComponentRdfEntityPickler getDnaComponentRdfPickler() {
        return dnaComponentRdfPickler;
      }
    };
  }

  private DnaComponentRdfEntityPickler configureDnaComponentRdfPickler(Properties props) {
    Properties dcProps = propertiesFor(props, "DnaComponent");

    return new DnaComponentRdfEntityPickler(configureSBOLNamedObjectRdfPickler(dcProps, configureSBOLObjectRdfPickler(dcProps)),
            getProperty(dcProps, "annotation"),
            getProperty(dcProps, "dnaSequence")) {
      @Override
      public DnaSequenceRdfEntityPickler getDnaSequenceRdfPickler() {
        return dnaSequenceRdfPickler;
      }

      @Override
      public SequenceAnnotationRdfEntityPickler getSequenceAnnotationRdfPickler() {
        return sequenceAnnotationRdfPickler;
      }
    };
  }

  private DnaSequenceRdfEntityPickler configureDnaSequenceRdfPickler(Properties props) {
    Properties dsProps = propertiesFor(props, "DnaSequence");

    return new DnaSequenceRdfEntityPickler(
            configureSBOLObjectRdfPickler(dsProps),
            getProperty(dsProps, "nucleotides"));
  }

  private SBOLNamedObjectRdfEntityPickler configureSBOLNamedObjectRdfPickler(Properties props, SBOLObjectRdfEntityPickler sbolObjectRdfPickler) {
    return new SBOLNamedObjectRdfEntityPickler(sbolObjectRdfPickler,
            getProperty(props, "name"), getProperty(props, "description"), getProperty(props, "displayId"));
  }

  private SBOLObjectRdfEntityPickler configureSBOLObjectRdfPickler(Properties props) {
    return new SBOLObjectRdfEntityPickler(getProperty(props, "type"));
  }

  private SequenceAnnotationRdfEntityPickler configureSequenceAnnotationRdfPickler(Properties props) {
    Properties saProps = propertiesFor(props, "SequenceAnnotation");

    SBOLObjectRdfEntityPickler sbolObjectRdfPickler = configureSBOLObjectRdfPickler(saProps);

    return new SequenceAnnotationRdfEntityPickler(sbolObjectRdfPickler,
            getProperty(saProps, "bioStart"),
            getProperty(saProps, "bioEnd"),
            getProperty(saProps, "strand"),
            getProperty(saProps, "precedes"),
            getProperty(saProps, "subComponent")) {
      @Override
      public DnaComponentRdfEntityPickler getDnaComponentRdfPickler() {
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
