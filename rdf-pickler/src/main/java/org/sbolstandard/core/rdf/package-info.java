/**
 * RDF Pickling API.
 *
 * <p>
 *   This package is the API and user-facing utilities for pickling SBOL objects to RDF.
 * </p>
 *
 * <ul>
 *   <li>To pickle SBOL objects using the default RDF terminology, use
 *   {@link org.sbolstandard.core.rdf.CoreRdfPicklers#instance()}.</li>
 *   <li>To build your own custom picklers, use the factory methods in
 *   {@link org.sbolstandard.core.rdf.RdfPicklers}.</li>
 *   <li>Low-level implementation detail can be found in the {@link org.sbolstandard.core.rdf.impl} package.</li>
 * </ul>
 *
 * @author Matthew Pocock
 */
package org.sbolstandard.core.rdf;