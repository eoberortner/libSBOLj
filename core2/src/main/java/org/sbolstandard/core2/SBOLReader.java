package org.sbolstandard.core2;

import static uk.ac.ncl.intbio.core.datatree.Datatree.NamespaceBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonReader;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.sbolstandard.core2.ComponentInstance.AccessType;
import org.sbolstandard.core2.MapsTo.RefinementType;
import org.sbolstandard.core2.SequenceConstraint.RestrictionType;

import uk.ac.intbio.core.io.turtle.TurtleIo;
import uk.ac.ncl.intbio.core.datatree.DocumentRoot;
import uk.ac.ncl.intbio.core.datatree.IdentifiableDocument;
import uk.ac.ncl.intbio.core.datatree.Literal;
import uk.ac.ncl.intbio.core.datatree.NamedProperty;
import uk.ac.ncl.intbio.core.datatree.NamespaceBinding;
import uk.ac.ncl.intbio.core.datatree.NestedDocument;
import uk.ac.ncl.intbio.core.datatree.TopLevelDocument;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.json.JsonIo;
import uk.ac.ncl.intbio.core.io.json.StringifyQName;
import uk.ac.ncl.intbio.core.io.rdf.RdfIo;

/**
 * @author Tramy Nguyen
 *
 */
public class SBOLReader
{
	static class SBOLPair
	{
		private URI left;
		private URI right;

		public SBOLPair(URI left, URI right)
		{
			this.left = left;
			this.right = right;
		}

		public URI getLeft() {
			return left;
		}

		public void setLeft(URI left) {
			this.left = left;
		}

		public URI getRight() {
			return right;
		}

		public void setRight(URI right) {
			this.right = right;
		}
	} //end of SBOLPair class

	private static String setURIPrefix	= null;

	/**
	 * Set the specified authority as the prefix to all member's identity
	 */
	public static void setURIPrefix(String authority)
	{
		SBOLReader.setURIPrefix = authority;
	}

	/**
	 * Takes in the given RDF filename and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument read(String fileName) throws Throwable
	{
		//FileInputStream fis 	 = new FileInputStream(fileName);
		//String inputStreamString = new Scanner(fis, "UTF-8").useDelimiter("\\A").next();
		return readRDF(new File(fileName));
	}

	/**
	 * Takes in the given JSON filename and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument readJSON(String fileName) throws Throwable
	{
		return readJSON(new File(fileName));
	}

	/**
	 * Takes in the given RDF filename and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument readRDF(String fileName) throws Throwable
	{
		return readRDF(new File(fileName));
	}

	/**
	 * Takes in the given Turtle filename and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument readTurtle(String fileName) throws Throwable
	{
		return readTurtle(new File(fileName));
	}

	/**
	 * Takes in the given JSON file and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument readJSON(File file) throws Throwable
	{
		FileInputStream stream 	   = new FileInputStream(file);
		BufferedInputStream buffer = new BufferedInputStream(stream);

		return readJSON(buffer);
	}

	/**
	 * Takes in the given RDF file and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument read(File file) throws Throwable
	{
		FileInputStream stream 	   = new FileInputStream(file);
		BufferedInputStream buffer = new BufferedInputStream(stream);

		return read(buffer);
	}

	/**
	 * Takes in the given RDF file and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument readRDF(File file) throws Throwable
	{
		FileInputStream stream     = new FileInputStream(file);
		BufferedInputStream buffer = new BufferedInputStream(stream);
		return readRDF(buffer);
	}

	/**
	 * Takes in the given Turtle file and converts the file to an SBOLDocument
	 * @throws Throwable
	 */
	public static SBOLDocument readTurtle(File file) throws Throwable
	{
		FileInputStream stream 	   = new FileInputStream(file);
		BufferedInputStream buffer = new BufferedInputStream(stream);

		return readTurtle(buffer);
	}

	/**
	 * Takes in a given JSON InputStream and converts the file to an SBOLDocument
	 */
	public static SBOLDocument readJSON(InputStream in)
	{
		Scanner scanner = new Scanner(in, "UTF-8");
		String inputStreamString = scanner.useDelimiter("\\A").next();
		SBOLDocument SBOLDoc     = new SBOLDocument();
		try
		{
			DocumentRoot<QName> document = readJSON(new StringReader(inputStreamString));

			for (NamespaceBinding n : document.getNamespaceBindings())
			{
				if (n.getNamespaceURI().equals(Sbol1Terms.sbol1.getNamespaceURI()))
				{
					scanner.close();
					return readRDFV1(in, document);
				}
				SBOLDoc.addNamespaceBinding(NamespaceBinding(n.getNamespaceURI(), n.getPrefix()));
				//				SBOLDoc.addNamespaceBinding(URI.create(n.getNamespaceURI()), n.getPrefix());
			}

			readTopLevelDocs(SBOLDoc, document);

		}
		catch (Exception e)
		{
			scanner.close();
			e.printStackTrace();
		}
		scanner.close();
		return SBOLDoc;
	}

	/**
	 * Takes in a given RDF InputStream and converts the file to an SBOLDocument
	 */
	public static SBOLDocument read(InputStream in)
	{
		Scanner scanner = new Scanner(in, "UTF-8");
		String inputStreamString = scanner.useDelimiter("\\A").next();
		SBOLDocument SBOLDoc     = new SBOLDocument();
		try
		{
			DocumentRoot<QName> document = readRDF(new StringReader(inputStreamString));

			for (NamespaceBinding n : document.getNamespaceBindings())
			{
				if (n.getNamespaceURI().equals(Sbol1Terms.sbol1.getNamespaceURI()))
				{
					scanner.close();
					return readRDFV1(in, document);
				}
				SBOLDoc.addNamespaceBinding(NamespaceBinding(n.getNamespaceURI(), n.getPrefix()));
				//				SBOLDoc.addNamespaceBinding(URI.create(n.getNamespaceURI()), n.getPrefix());
			}

			readTopLevelDocs(SBOLDoc, document);

		}
		catch (Exception e)
		{
			scanner.close();
			e.printStackTrace();
		}

		scanner.close();
		return SBOLDoc;
	}

	/**
	 * Takes in a given RDF InputStream and converts the file to an SBOLDocument
	 */
	public static SBOLDocument readRDF(InputStream in)
	{
		Scanner scanner = new Scanner(in, "UTF-8");
		String inputStreamString = scanner.useDelimiter("\\A").next();
		SBOLDocument SBOLDoc     = new SBOLDocument();

		try
		{
			DocumentRoot<QName> document = readRDF(new StringReader(inputStreamString));
			for (NamespaceBinding n : document.getNamespaceBindings())
			{
				if (n.getNamespaceURI().equals(Sbol1Terms.sbol1.getNamespaceURI()))
				{
					scanner.close();
					return readRDFV1(in, document);
				}
				SBOLDoc.addNamespaceBinding(NamespaceBinding(n.getNamespaceURI(), n.getPrefix()));
				//				SBOLDoc.addNamespaceBinding(URI.create(n.getNamespaceURI()), n.getPrefix());
			}
			readTopLevelDocs(SBOLDoc, document);
		}
		catch (Exception e)
		{
			scanner.close();
			e.printStackTrace();
		}
		scanner.close();
		return SBOLDoc;
	}

	/**
	 * Takes in a given Turtle InputStream and converts the file to an SBOLDocument
	 */
	public static SBOLDocument readTurtle(InputStream in)
	{
		Scanner scanner = new Scanner(in, "UTF-8");
		String inputStreamString = scanner.useDelimiter("\\A").next();
		SBOLDocument SBOLDoc     = new SBOLDocument();

		try
		{
			DocumentRoot<QName> document = readTurtle(new StringReader(inputStreamString));
			for (NamespaceBinding n : document.getNamespaceBindings())
			{
				if (n.getNamespaceURI().equals(Sbol1Terms.sbol1.getNamespaceURI()))
				{
					scanner.close();
					return readRDFV1(in, document);
				}
				SBOLDoc.addNamespaceBinding(NamespaceBinding(n.getNamespaceURI(), n.getPrefix()));
				//				SBOLDoc.addNamespaceBinding(URI.create(n.getNamespaceURI()), n.getPrefix());

			}
			readTopLevelDocs(SBOLDoc, document);
		}
		catch (Exception e)
		{
			scanner.close();
			e.printStackTrace();
		}

		scanner.close();
		return SBOLDoc;
	}

	private static SBOLDocument readRDFV1(InputStream in, DocumentRoot<QName> document)
	{
		SBOLDocument SBOLDoc = new SBOLDocument();
		for (NamespaceBinding n : document.getNamespaceBindings())
		{
			if (n.getNamespaceURI().equals(Sbol1Terms.sbol1.getNamespaceURI()))
			{
				SBOLDoc.addNamespaceBinding(NamespaceBinding(Sbol2Terms.sbol2.getNamespaceURI(),
								Sbol2Terms.sbol2.getPrefix()));
			}
			else
			{
				SBOLDoc.addNamespaceBinding(
						NamespaceBinding(n.getNamespaceURI(), n.getPrefix()));
			}
		}
		SBOLDoc.addNamespaceBinding(NamespaceBinding(Sbol2Terms.prov.getNamespaceURI(),
				Sbol2Terms.prov.getPrefix()));
		readTopLevelDocsV1(SBOLDoc, document);
		return SBOLDoc;
	}

	private static DocumentRoot<QName> readJSON(Reader stream) throws Exception
	{
		JsonReader reader 		  = Json.createReaderFactory(Collections.<String, Object> emptyMap()).createReader(stream);
		JsonIo jsonIo 	  		  = new JsonIo();
		IoReader<String> ioReader = jsonIo.createIoReader(reader.read());
		DocumentRoot<String> root = ioReader.read();
		return StringifyQName.string2qname.mapDR(root);
	}

	private static DocumentRoot<QName> readRDF(Reader reader) throws Exception
	{
		XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(reader);
		RdfIo rdfIo 			  = new RdfIo();
		return rdfIo.createIoReader(xmlReader).read();
	}

	private static DocumentRoot<QName> readTurtle(Reader reader) throws Exception
	{
		TurtleIo turtleIo = new TurtleIo();
		return turtleIo.createIoReader(reader).read();
	}

	private static void readTopLevelDocsV1(SBOLDocument SBOLDoc, DocumentRoot<QName> document)
	{
		for (TopLevelDocument<QName> topLevel : document.getTopLevelDocuments())
		{
			if (topLevel.getType().equals(Sbol1Terms.DNAComponent.DNAComponent))
				parseDnaComponentV1(SBOLDoc, topLevel);
			else if (topLevel.getType().equals(Sbol1Terms.DNASequence.DNASequence))
				parseDnaSequenceV1(SBOLDoc, topLevel);
			else if (topLevel.getType().equals(Sbol1Terms.Collection.Collection))
				parseCollectionV1(SBOLDoc, topLevel);
			else
			{
				parseGenericTopLevel(SBOLDoc, topLevel);
			}
		}
	}

	private static void readTopLevelDocs(SBOLDocument SBOLDoc, DocumentRoot<QName> document)
	{
		for (TopLevelDocument<QName> topLevel : document.getTopLevelDocuments())
		{
			if (topLevel.getType().equals(Sbol2Terms.Collection.Collection))
				parseCollections(SBOLDoc, topLevel);
			else if (topLevel.getType().equals(Sbol2Terms.ModuleDefinition.ModuleDefinition))
				parseModuleDefinition(SBOLDoc, topLevel);
			else if (topLevel.getType().equals(Sbol2Terms.Model.Model))
				parseModels(SBOLDoc, topLevel);
			else if (topLevel.getType().equals(Sbol2Terms.Sequence.Sequence))
				parseSequences(SBOLDoc, topLevel);
			else if (topLevel.getType().equals(Sbol2Terms.ComponentDefinition.ComponentDefinition))
				parseComponentDefinition(SBOLDoc, topLevel);
			else
				parseGenericTopLevel(SBOLDoc, topLevel);
		}
	}

	private static ComponentDefinition parseDnaComponentV1(
			SBOLDocument SBOLDoc, IdentifiableDocument<QName> componentDef)
	{
		String displayId   = null;
		String name 	   = null;
		String description = null;
		URI seq_identity   = null;
		Set<URI> roles 	   = new HashSet<>();
		URI identity 	   = componentDef.getIdentity();
		String persIdentity = "";

		List<Annotation> annotations 				 = new ArrayList<>();
		List<SequenceAnnotation> sequenceAnnotations = new ArrayList<>();
		List<Component> components 					 = new ArrayList<>();
		List<SequenceConstraint> sequenceConstraints = new ArrayList<>();
		List<SBOLPair> precedePairs 				 = new ArrayList<>();
		Map<URI, URI> componentDefMap 				 = new HashMap<>();

		Set<URI> type = new HashSet<>();
		type.add(Sbol2Terms.DnaComponentV1URI.type);

		int component_num = 0;
		int sa_num 		  = 0;

		for (NamedProperty<QName> namedProperty : componentDef.getProperties())
		{
			if (namedProperty.getName().equals(Sbol1Terms.DNAComponent.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				if (setURIPrefix != null ) //TODO: check version set
				{
					persIdentity = setURIPrefix + "/" + SBOLDocument.TopLevelTypes.componentDefinition +
							"/" + displayId;
					identity = URI.create(persIdentity + "/1.0");
				}
			}
			else if (namedProperty.getName().equals(Sbol1Terms.DNAComponent.name))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol1Terms.DNAComponent.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol1Terms.DNAComponent.type))
			{
				roles.add(URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString()));
			}
			else if (namedProperty.getName().equals(Sbol1Terms.DNAComponent.annotations))
			{
				SequenceAnnotation sa = parseSequenceAnnotationV1(SBOLDoc,
						((NestedDocument<QName>) namedProperty.getValue()),
						precedePairs, persIdentity, ++sa_num);

				sequenceAnnotations.add(sa);
				// TODO: if version then + "/" + version, else skip version

				URI component_identity    = URI.create(persIdentity + "/component" + ++component_num + "/1.0");
				AccessType access 		  = AccessType.PUBLIC;
				URI instantiatedComponent = sa.getComponentURI();
				URI originalURI 		  = ((NestedDocument<QName>) namedProperty.getValue()).getIdentity();

				componentDefMap.put(originalURI, component_identity);
				sa.setComponent(component_identity);

				Component component = new Component(component_identity, access, instantiatedComponent);
				if (!persIdentity.equals("")) {
					component.setPersistentIdentity(URI.create(persIdentity+"/component"+component_num));
					component.setDisplayId("component"+component_num);
					component.setVersion("1.0");
				}
				components.add(component);
			}
			else if (namedProperty.getName().equals(Sbol1Terms.DNAComponent.dnaSequence))
			{
				seq_identity = parseDnaSequenceV1(SBOLDoc,
						(NestedDocument<QName>) namedProperty.getValue()).getIdentity();
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		if (roles.isEmpty())
			roles.add(Sbol2Terms.DnaComponentV1URI.roles);

		int sc_number = 0;

		for (SBOLPair pair : precedePairs)
		{
			URI sc_identity    			= URI.create(persIdentity + "/sequenceConstraint" + ++sc_number + "/1.0");
			URI restrictionURI 			= Sbol2Terms.DnaComponentV1URI.restriction;
			RestrictionType restriction = SequenceConstraint.RestrictionType.convertToRestrictionType(restrictionURI);

			URI subject = null;
			URI object  = null;

			for (URI key : componentDefMap.keySet())
			{
				if (pair.getLeft().equals(key))
				{
					subject = componentDefMap.get(key);
				}
				else if (pair.getRight().equals(key))
				{
					object = componentDefMap.get(key);
				}
			}

			SequenceConstraint sc = new SequenceConstraint(sc_identity, restriction, subject, object);
			if (!persIdentity.equals("")) {
				sc.setPersistentIdentity(URI.create(persIdentity+"/sequenceConstraint"+sc_number));
				sc.setDisplayId("sequenceConstraint"+sc_number);
				sc.setVersion("1.0");
			}
			sequenceConstraints.add(sc);
		}

		//ComponentDefinition c = SBOLDoc.createComponentDefinition(identity, type, roles);
		ComponentDefinition c = SBOLDoc.createComponentDefinition(identity, type);
		// todo: is roles ever not null by the time you get here?
		if (!persIdentity.equals("")) {
			c.setPersistentIdentity(URI.create(persIdentity));
			c.setVersion("1.0");
		}
		if(roles != null)
			c.setRoles(roles);
		if(identity != componentDef.getIdentity())
			c.setWasDerivedFrom(componentDef.getIdentity());
		if (displayId != null)
			c.setDisplayId(displayId);
		if (name != null && !name.isEmpty())
			c.setName(name);
		if (description != null && !description.isEmpty())
			c.setDescription(description);
		if (seq_identity != null)
			c.setSequence(seq_identity);
		if (!annotations.isEmpty())
			c.setAnnotations(annotations);
		if (!sequenceAnnotations.isEmpty())
			c.setSequenceAnnotations(sequenceAnnotations);
		if (!components.isEmpty())
			c.setComponents(components);
		if (!sequenceConstraints.isEmpty())
			c.setSequenceConstraints(sequenceConstraints);

		return c;
	}

	private static Sequence parseDnaSequenceV1(SBOLDocument SBOLDoc, IdentifiableDocument<QName> topLevel)
	{
		String elements    = null;
		String displayId   = null;
		String name   	   = null;
		String description = null;
		URI identity 	   = topLevel.getIdentity();
		URI persistentIdentity = null;
		URI encoding 	   = Sbol2Terms.SequenceURI.DnaSequenceV1;
		List<Annotation> annotations = new ArrayList<>();

		if (setURIPrefix != null)
		{
			if (topLevel.getIdentity().toString().lastIndexOf('/') != -1)
			{
				displayId = topLevel.getIdentity().toString().substring(topLevel.getIdentity().toString().lastIndexOf('/') + 1);
				identity = URI.create(setURIPrefix + "/" + SBOLDocument.TopLevelTypes.sequence + "/" + displayId + "/1.0");
				persistentIdentity = URI.create(setURIPrefix + "/" + SBOLDocument.TopLevelTypes.sequence + "/" + displayId);
			}
		}

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol1Terms.DNASequence.nucleotides))
			{
				elements = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				if (setURIPrefix != null)
				{
					identity = URI.create(setURIPrefix + "/" + displayId + "/1.0");
				}
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Sequence sequence = SBOLDoc.createSequence(identity, elements, encoding);
		if(persistentIdentity!=null) {
			sequence.setPersistentIdentity(persistentIdentity);
			sequence.setVersion("1.0");
		}
		if(identity != topLevel.getIdentity())
			sequence.setWasDerivedFrom(topLevel.getIdentity());
		if (displayId != null)
			sequence.setDisplayId(displayId);
		if (name != null)
			sequence.setName(name);
		if (description != null)
			sequence.setDescription(description);
		if (!annotations.isEmpty())
			sequence.setAnnotations(annotations);

		return sequence;
	}

	private static Collection parseCollectionV1(SBOLDocument SBOLDoc, IdentifiableDocument<QName> topLevel)
	{
		URI identity 	   = topLevel.getIdentity();
		URI persistentIdentity = null;
		String displayId   = null;
		String name 	   = null;
		String description = null;

		Set<URI> members 			 = new HashSet<>();
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol1Terms.Collection.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				if (setURIPrefix != null)
				{
					identity = URI.create(setURIPrefix + "/" + SBOLDocument.TopLevelTypes.collection + "/" +
							displayId + "/1.0");
					persistentIdentity = URI.create(setURIPrefix + "/" + SBOLDocument.TopLevelTypes.collection + "/" +
							displayId);
				}
			}
			else if (namedProperty.getName().equals(Sbol1Terms.Collection.name))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol1Terms.Collection.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol1Terms.Collection.component))
			{
				members.add(parseDnaComponentV1(SBOLDoc,
						(NestedDocument<QName>) namedProperty.getValue()).getIdentity());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Collection c = SBOLDoc.createCollection(identity);
		if (persistentIdentity!=null) {
			c.setPersistentIdentity(persistentIdentity);
			c.setVersion("1.0");
		}
		if(identity != topLevel.getIdentity())
			c.setWasDerivedFrom(topLevel.getIdentity());
		if (displayId != null)
			c.setDisplayId(displayId);
		if (name != null)
			c.setName(name);
		if (description != null)
			c.setDescription(description);
		if (!members.isEmpty())
			c.setMembers(members);
		if (!annotations.isEmpty())
			c.setAnnotations(annotations);
		return c;
	}

	private static SequenceAnnotation parseSequenceAnnotationV1(
			SBOLDocument SBOLDoc, NestedDocument<QName> sequenceAnnotation,
			List<SBOLPair> precedePairs, String parentURI, int sa_num)
	{
		Integer start 	 = null;
		Integer end 	 = null;
		String strand    = null;
		URI componentURI = null;
		URI identity 	 = sequenceAnnotation.getIdentity();
		String persIdentity = "";
		List<Annotation> annotations = new ArrayList<>();

		if (setURIPrefix != null)
		{
			persIdentity = parentURI + "/annotation" + sa_num;
			identity = URI.create(persIdentity + "/1.0");
		}
		for (NamedProperty<QName> namedProperty : sequenceAnnotation.getProperties())
		{
			if (namedProperty.getName().equals(Sbol1Terms.SequenceAnnotations.bioStart))
			{
				String temp = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				start = Integer.parseInt(temp);
			}
			else if (namedProperty.getName().equals(Sbol1Terms.SequenceAnnotations.bioEnd))
			{
				String temp2 = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				end = Integer.parseInt(temp2);
			}
			else if (namedProperty.getName().equals(Sbol1Terms.SequenceAnnotations.strand))
			{
				strand = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol1Terms.SequenceAnnotations.subComponent))
			{
				componentURI = parseDnaComponentV1(SBOLDoc,
						(NestedDocument<QName>) namedProperty.getValue()).getIdentity();
			}
			else if (namedProperty.getName().equals(Sbol1Terms.SequenceAnnotations.precedes))
			{
				URI left 	  = sequenceAnnotation.getIdentity();
				URI right 	  = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
				SBOLPair pair = new SBOLPair(left, right);
				precedePairs.add(pair);
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Location location = null; // Note: Do not create a seqAnnotation if Location is empty

		if (start != null && end != null) // create SequenceAnnotation & Component
		{
			URI range_identity = URI.create(persIdentity + "/range/1.0");
			Range r = new Range(range_identity, start, end);
			if (!persIdentity.equals("")) {
				r.setPersistentIdentity(URI.create(persIdentity+"/range"));
				r.setDisplayId("range");
				r.setVersion("1.0");
			}
			if (strand != null)
			{
				if (strand.equals("+"))
				{
					r.setOrientation(Sbol2Terms.Orientation.inline);
				}
				else if (strand.equals("-"))
				{
					r.setOrientation(Sbol2Terms.Orientation.reverseComplement);
				}

				location = r;
			}
		}
		else
		{
			URI dummyGenericLoc_id = URI.create(persIdentity + "/GenericLocation/1.0");
			GenericLocation  dummyGenericLoc = new GenericLocation(dummyGenericLoc_id);
			if (!persIdentity.equals("")) {
				dummyGenericLoc.setPersistentIdentity(URI.create(persIdentity+"/GenericLocation"));
				dummyGenericLoc.setDisplayId("range");
				dummyGenericLoc.setVersion("1.0");
			}
			if (strand != null)
			{
				if (strand.equals("+"))
				{
					dummyGenericLoc.setOrientation(Sbol2Terms.Orientation.inline);
				}
				else if (strand.equals("-"))
				{
					dummyGenericLoc.setOrientation(Sbol2Terms.Orientation.reverseComplement);
				}
				location = dummyGenericLoc;
			}
		}

		SequenceAnnotation s = new SequenceAnnotation(identity, location);
		if(!persIdentity.equals("")) {
			s.setPersistentIdentity(URI.create(persIdentity));
			s.setDisplayId("annotation" + sa_num);
			s.setVersion("1.0");
		}
		if(identity != sequenceAnnotation.getIdentity())
			s.setWasDerivedFrom(sequenceAnnotation.getIdentity());
		if (componentURI != null)
			s.setComponent(componentURI);
		if (!annotations.isEmpty())
			s.setAnnotations(annotations);

		return s;
	}


	private static void populateIdentified(
			IdentifiableDocument.Properties<QName> props, Identified toMake)
	{
		toMake.setPersistentIdentity(props.uri().getOptionalValue(
				Sbol2Terms.Identified.persistentIdentity));
		toMake.setVersion(props.string().getOptionalValue(
				Sbol2Terms.Identified.version));
		toMake.setWasDerivedFrom(props.uri().getOptionalValue(
				Sbol2Terms.Identified.wasDerivedFrom));
	}


	private static void populateDocumented(
			IdentifiableDocument.Properties<QName> props, Documented toMake)
	{
		toMake.setDisplayId(props.string().getOptionalValue(
				Sbol2Terms.Documented.displayId));
		toMake.setName(props.string().getOptionalValue(
				Sbol2Terms.Documented.title));
		toMake.setDescription(props.string().getOptionalValue(
				Sbol2Terms.Documented.description));
	}


	private static void populateTopLevel(
			IdentifiableDocument.Properties<QName> props, TopLevel toMake)
	{
		// nothing to do
	}


	private static void populateComponentDefinition(
			IdentifiableDocument.Properties<QName> props, ComponentDefinition toMake)
	{
		toMake.setSequence(props.uri().getOptionalValue(Sbol2Terms.ComponentDefinition.hasSequence));

		Set<URI> types = new HashSet<>();
		for(URI t : props.uri().getValues(Sbol2Terms.ComponentDefinition.type)) {
			types.add(t);
		}
		toMake.setTypes(types);

		Set<URI> roles = new HashSet<>();
		for(URI r : props.uri().getValues(Sbol2Terms.ComponentDefinition.roles)) {
			roles.add(r);
		}
		toMake.setRoles(roles);

		List<Component> components = new ArrayList<>();
		for(NestedDocument<QName> doc : props.nestedDocument().getValues(Sbol2Terms.ComponentDefinition.hasComponent)) {
			components.add(parseSubComponent(doc));
		}
		toMake.setComponents(components);

		List<SequenceAnnotation> sequenceAnnotations = new ArrayList<>();
		for(NestedDocument<QName> doc : props.nestedDocument().getValues(Sbol2Terms.ComponentDefinition.hasSequenceAnnotations)) {
			sequenceAnnotations.add(parseSequenceAnnotation(doc));
		}
		toMake.setSequenceAnnotations(sequenceAnnotations);

		List<SequenceConstraint> sequenceConstraints = new ArrayList<>();
		for(NestedDocument<QName> doc : props.nestedDocument().getValues(Sbol2Terms.ComponentDefinition.hasSequenceConstraints)) {
			sequenceConstraints.add(parseSequenceConstraint(doc));
		}
		toMake.setSequenceConstraints(sequenceConstraints);
	}


	private static ComponentDefinition parseComponentDefinition(
			SBOLDocument sbolDoc, TopLevelDocument<QName> doc)
	{
		final IdentifiableDocument.Properties<QName> properties = doc.properties();
		final ComponentDefinition toMake = sbolDoc.createComponentDefinition(doc.getIdentity());

		populateIdentified(properties, toMake);
		populateDocumented(properties, toMake);
		populateTopLevel(properties, toMake);
		populateComponentDefinition(properties, toMake);
		populateAnnotations(properties, toMake, Sbol2Terms.ComponentDefinition.all);

		return toMake;
	}


	private static void populateSequenceConstraint(
			IdentifiableDocument.Properties<QName> props, SequenceConstraint toMake)
	{
		toMake.setRestriction(props.uri().getValue(
				Sbol2Terms.SequenceConstraint.restriction));
		toMake.setSubject(props.uri().getValue(
				Sbol2Terms.SequenceConstraint.hasSubject));
		toMake.setObject(props.uri().getValue(
				Sbol2Terms.SequenceConstraint.hasObject));
	}


	private static SequenceConstraint parseSequenceConstraint(NestedDocument<QName> doc)
	{
		final IdentifiableDocument.Properties<QName> properties = doc.properties();
		final SequenceConstraint toMake = new SequenceConstraint(doc.getIdentity());
		
		populateIdentified(properties, toMake);
		populateSequenceConstraint(properties, toMake);
		populateAnnotations(properties, toMake, Sbol2Terms.SequenceConstraint.all);

		return toMake;
	}


	private static void populateSequenceAnnotation(
			IdentifiableDocument.Properties<QName> props, SequenceAnnotation toMake)
	{
		toMake.setComponent(props.uri().getOptionalValue(
				Sbol2Terms.SequenceAnnotation.hasComponent));
		toMake.setLocation(parseLocation(props.nestedDocument().getValue(
				Sbol2Terms.SequenceAnnotation.hasLocation)));
	}


	private static SequenceAnnotation parseSequenceAnnotation(NestedDocument<QName> doc)
	{
		final IdentifiableDocument.Properties<QName> properties = doc.properties();
		final SequenceAnnotation toMake = new SequenceAnnotation(doc.getIdentity());

		populateIdentified(properties, toMake);
		populateDocumented(properties, toMake);
		populateSequenceAnnotation(properties, toMake);
		populateAnnotations(properties, toMake, Sbol2Terms.SequenceAnnotation.all);

		return toMake;
	}

	private static Location parseLocation(NestedDocument<QName> location)
	{
		Location l 					 = null;
		List<Annotation> annotations = new ArrayList<>();

		if (location.getType().equals(Sbol2Terms.Range.Range))
		{
			l = parseRange(location);
		}
		else if (location.getType().equals(Sbol2Terms.MultiRange.MultiRange))
		{
			l = parseMultiRange(location);
		}
		else if (location.getType().equals(Sbol2Terms.Cut.Cut))
		{
			l = parseCut(location);
		}
		else if (location.getType().equals(Sbol2Terms.GenericLocation.GenericLocation))
		{
			l = parseGenericLocation(location);
		}
		else
		{
			System.out.println("ERR: Null. Location isn't a Range, MultiRange, or Cut.");
			return l; // codereview: this is always null? do you mean this?
		}

		// codereview: this is brittle for NPEs if your if/else cascade higher up gets out of sync with the data model
		if (!annotations.isEmpty())
			l.setAnnotations(annotations);

		return l;

	}

	private static GenericLocation parseGenericLocation(NestedDocument<QName> typeGenLoc)
	{
		URI persistentIdentity       = null;
		String displayId			 = null;
		URI orientation 			 = null;
		String version        	     = null;
		URI wasDerivedFrom 			 = null;
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : typeGenLoc.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.GenericLocation.orientation))
			{
				orientation = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		GenericLocation gl = new GenericLocation(typeGenLoc.getIdentity());
		if(displayId != null)
			gl.setDisplayId(displayId);
		if(orientation != null)
			gl.setOrientation(orientation);
		if(persistentIdentity != null)
			gl.setPersistentIdentity(persistentIdentity);
		if(version != null)
			gl.setVersion(version);
		if(wasDerivedFrom != null)
			gl.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			gl.setAnnotations(annotations);

		return gl;
	}

	private static Cut parseCut(NestedDocument<QName> typeCut)
	{
		URI persistentIdentity = null;
		String displayId	   = null;
		Integer at 			   = null;
		URI orientation 	   = null;
		String version 		   = null;
		URI wasDerivedFrom 	   = null;
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : typeCut.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Cut.at))
			{
				String temp = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				at 			= Integer.parseInt(temp);
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Cut.orientation))
			{
				orientation = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		if (at == null)
		{
			// TODO: how to handle problems with data model when at is null
			System.out.println("ERR: at is Null. Problem is data model");
		}

		Cut c = new Cut(typeCut.getIdentity(), at);
		if (persistentIdentity != null)
			c.setPersistentIdentity(persistentIdentity);
		if (displayId != null)
			c.setDisplayId(displayId);
		if (orientation != null)
			c.setOrientation(orientation);
		if(version != null)
			c.setVersion(version);
		if (wasDerivedFrom != null)
			c.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			c.setAnnotations(annotations);

		return c;
	}

	private static MultiRange parseMultiRange(NestedDocument<QName> typeMultiRange)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		URI wasDerivedFrom 	   = null;
		String displayId       = null;

		List<Range> ranges 	 		 = new ArrayList<>();
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : typeMultiRange.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.MultiRange.hasRanges))
			{
				ranges.add(parseRange((NestedDocument<QName>) namedProperty.getValue()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		MultiRange multiRange = new MultiRange(typeMultiRange.getIdentity(),ranges);
		if (displayId != null)
			multiRange.setDisplayId(displayId);
		if (persistentIdentity != null)
			multiRange.setPersistentIdentity(persistentIdentity);
		if(version != null)
			multiRange.setVersion(version);
		if (wasDerivedFrom != null)
			multiRange.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			multiRange.setAnnotations(annotations);
		return null;
	}

	private static Range parseRange(NestedDocument<QName> typeRange)
	{
		URI persistentIdentity = null;
		String displayId       = null;
		Integer start 		   = null;
		Integer end 		   = null;
		URI orientation 	   = null;
		String version 		   = null;
		URI wasDerivedFrom     = null;
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : typeRange.getProperties())
		{
			String temp;
			if (namedProperty.getName().equals(Sbol2Terms.Range.start))
			{
				temp  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				start = Integer.parseInt(temp);
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Range.end))
			{
				temp = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
				end  = Integer.parseInt(temp);
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Range.orientation))
			{
				orientation = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Range r = new Range(typeRange.getIdentity(), start, end);
		if (displayId != null)
			r.setDisplayId(displayId);
		if (persistentIdentity != null)
			r.setPersistentIdentity(persistentIdentity);
		if (orientation != null)
			r.setOrientation(orientation);
		if(version != null)
			r.setVersion(version);
		if (wasDerivedFrom != null)
			r.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			r.setAnnotations(annotations);
		return r;
	}

	private static Component parseSubComponent(NestedDocument<QName> subComponents)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		String displayId 	   = null;
		String name 		   = null;
		String description 	   = null;
		URI subComponentURI    = null;
		AccessType access 	   = null;
		URI wasDerivedFrom 	   = null;

		List<Annotation> annotations = new ArrayList<>();
		List<MapsTo> mapsTo 		 = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : subComponents.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ComponentInstance.access))
			{
				access = ComponentInstance.AccessType.convertToAccessType(URI
						.create(((Literal<QName>) namedProperty.getValue())
								.getValue().toString()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Module.hasMapsTo))
			{
				mapsTo.add(parseMapsTo((NestedDocument<QName>) namedProperty.getValue()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ComponentInstance.hasComponentDefinition))
			{
				subComponentURI = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Component c = new Component(subComponents.getIdentity(), access, subComponentURI);
		if (persistentIdentity != null)
			c.setPersistentIdentity(persistentIdentity);
		if(version != null)
			c.setVersion(version);
		if (displayId != null)
			c.setDisplayId(displayId);
		if (access != null)
			c.setAccess(access);
		if (!mapsTo.isEmpty())
			c.setMapsTo(mapsTo);
		if (subComponentURI != null)
			c.setDefinition(subComponentURI);
		if (name != null)
			c.setName(name);
		if (description != null)
			c.setDescription(description);
		if (wasDerivedFrom != null)
			c.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			c.setAnnotations(annotations);

		return c;
	}

	private static GenericTopLevel parseGenericTopLevel(SBOLDocument SBOLDoc,
			TopLevelDocument<QName> topLevel)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		String displayId 	   = null;
		String name 		   = null;
		String description 	   = null;
		URI wasDerivedFrom 	   = null;

		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		GenericTopLevel t = SBOLDoc.createGenericTopLevel(topLevel.getIdentity(), topLevel.getType());
		if (persistentIdentity != null)
			t.setPersistentIdentity(persistentIdentity);
		if (version != null)
			t.setVersion(version);
		if (displayId != null)
			t.setDisplayId(displayId);
		if (name != null)
			t.setName(name);
		if (description != null)
			t.setDescription(description);
		if (wasDerivedFrom != null)
			t.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			t.setAnnotations(annotations);
		return t;
	}

	private static Model parseModels(SBOLDocument SBOLDoc, TopLevelDocument<QName> topLevel)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		String displayId 	   = null;
		String name 		   = null;
		String description 	   = null;
		URI source 			   = null;
		URI language 		   = null;
		URI framework 	 	   = null;
		URI wasDerivedFrom 	   = null;

		// codereview: do you ever save `roles` anywhere?
		Set<URI> roles 				 = new HashSet<>();
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Model.source))
			{
				source = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Model.language))
			{
				language = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Model.framework))
			{
				framework = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Model.roles))
			{
				roles.add(URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		//Model m = SBOLDoc.createModel(topLevel.getIdentity(), source, language, framework, roles);
		Model m = SBOLDoc.createModel(topLevel.getIdentity(), source, language, framework);
		if (persistentIdentity != null)
			m.setPersistentIdentity(persistentIdentity);
		if (version != null)
			m.setVersion(version);
		if (displayId != null)
			m.setDisplayId(displayId);
		if (name != null)
			m.setName(name);
		if (description != null)
			m.setDescription(description);
		if (wasDerivedFrom != null)
			m.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			m.setAnnotations(annotations);
		return m;
	}

	private static Collection parseCollections(SBOLDocument SBOLDoc, TopLevelDocument<QName> topLevel)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		String displayId 	   = null;
		String name 		   = null;
		String description 	   = null;
		URI wasDerivedFrom 	   = null;

		Set<URI> members 			 = new HashSet<>();
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Collection.hasMembers))
			{
				members.add(URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Collection c = SBOLDoc.createCollection(topLevel.getIdentity());
		if (displayId != null)
			c.setDisplayId(displayId);
		if (version != null)
			c.setVersion(version);
		if (persistentIdentity != null)
			c.setPersistentIdentity(persistentIdentity);
		if (!members.isEmpty())
			c.setMembers(members);
		if (name != null)
			c.setName(name);
		if (description != null)
			c.setDescription(description);
		if( wasDerivedFrom != null)
			c.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			c.setAnnotations(annotations);
		return c;
	}

	private static ModuleDefinition parseModuleDefinition(SBOLDocument SBOLDoc,
			TopLevelDocument<QName> topLevel)
	{
		URI persistentIdentity = null;
		String version 	       = null;
		String displayId 	   = null;
		String name 		   = null;
		String description 	   = null;
		URI wasDerivedFrom 	   = null;
		Set<URI> roles 		   = new HashSet<>();
		Set<URI> models 	   = new HashSet<>();

		List<FunctionalComponent> functionalComponents = new ArrayList<>();
		List<Interaction> interactions 				   = new ArrayList<>();
		List<Module> subModules 					   = new ArrayList<>();
		List<Annotation> annotations 				   = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ModuleDefinition.roles))
			{
				roles.add(URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ModuleDefinition.hasModule))
			{
				subModules.add(parseSubModule(((NestedDocument<QName>) namedProperty.getValue())));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ModuleDefinition.hasInteractions))
			{
				interactions.add(parseInteraction(((NestedDocument<QName>) namedProperty.getValue())));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ModuleDefinition.hasfunctionalComponent))
			{
				functionalComponents.add(parseFunctionalComponent((NestedDocument<QName>) namedProperty.getValue()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.ModuleDefinition.hasModels))
			{
				models.add(URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		ModuleDefinition moduleDefinition = SBOLDoc.createModuleDefinition(topLevel.getIdentity());
		if (roles != null) // codereview: is this ever not true?
			moduleDefinition.setRoles(roles);
		if (persistentIdentity != null)
			moduleDefinition.setPersistentIdentity(persistentIdentity);
		if (version != null)
			moduleDefinition.setVersion(version);
		if (displayId != null)
			moduleDefinition.setDisplayId(displayId);
		if (!functionalComponents.isEmpty())
			moduleDefinition.setFunctionalComponents(functionalComponents);
		if (!interactions.isEmpty())
			moduleDefinition.setInteractions(interactions);
		if (!models.isEmpty())
			moduleDefinition.setModels(models);
		if (!subModules.isEmpty())
			moduleDefinition.setModules(subModules);
		if (name != null)
			moduleDefinition.setName(name);
		if (description != null)
			moduleDefinition.setDescription(description);
		if (wasDerivedFrom != null)
			moduleDefinition.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			moduleDefinition.setAnnotations(annotations);
		return moduleDefinition;
	}

	private static Module parseSubModule(NestedDocument<QName> module)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		String displayId 	   = null;
		URI definitionURI 	   = null;
		String name 		   = null;
		String description     = null;
		URI wasDerivedFrom 	   = null;
		List<MapsTo> mappings 		 = new ArrayList<>();
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : module.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Module.hasMapsTo))
			{
				mappings.add(parseMapsTo((NestedDocument<QName>) namedProperty.getValue()));
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Module.hasDefinition))
			{
				definitionURI = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Module submodule = new Module(module.getIdentity(), definitionURI);
		if (persistentIdentity != null)
			submodule.setPersistentIdentity(persistentIdentity);
		if (version != null)
			submodule.setVersion(version);
		if (displayId != null)
			submodule.setDisplayId(displayId);
		if (!mappings.isEmpty())
			submodule.setMapsTos(mappings);
		if (name != null)
			submodule.setName(name);
		if (description != null)
			submodule.setDescription(description);
		if( wasDerivedFrom != null)
			submodule.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			submodule.setAnnotations(annotations);
		return submodule;
	}

	private static MapsTo parseMapsTo(NestedDocument<QName> mappings)
	{
		URI persistentIdentity    = null;
		String displayId		  = null;
		String version 	  	 	  = null;
		URI remote 				  = null;
		RefinementType refinement = null;
		URI local 				  = null;
		URI wasDerivedFrom 		  = null;

		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> m : mappings.getProperties())
		{
			if (m.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) m.getValue()).getValue().toString());
			}
			else if (m.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) m.getValue()).getValue().toString();
			}
			else if (m.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) m.getValue()).getValue().toString();
			}
			else if (m.getName().equals(Sbol2Terms.MapsTo.refinement))
			{
				refinement = RefinementType.valueOf(((Literal<QName>) m.getValue()).getValue().toString());
			}
			else if (m.getName().equals(Sbol2Terms.MapsTo.hasRemote))
			{
				remote = URI.create(((Literal<QName>) m.getValue()).getValue().toString());
			}
			else if (m.getName().equals(Sbol2Terms.MapsTo.hasLocal))
			{
				local = URI.create(((Literal<QName>) m.getValue()).getValue().toString());
			}
			else if (m.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) m.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(m));
			}
		}

		MapsTo map = new MapsTo(mappings.getIdentity(), refinement, local, remote);
		if (displayId != null)
			map.setDisplayId(displayId);
		if (persistentIdentity != null)
			map.setPersistentIdentity(persistentIdentity);
		if (version != null)
			map.setVersion(version);
		if (wasDerivedFrom != null)
			map.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			map.setAnnotations(annotations);
		return map;
	}

	private static void populateInteraction(IdentifiableDocument.Properties<QName> props, Interaction toMake) {
		final Set<URI> type 		 = new HashSet<>();
		for(URI t : props.uri().getValues(Sbol2Terms.Interaction.type)) {
			type.add(t);
		}
		toMake.setTypes(type);


		final List<Participation> participations = new ArrayList<>();
		for(NestedDocument<QName> nd : props.nestedDocument().getValues(Sbol2Terms.Interaction.hasParticipations)) {
			participations.add(parseParticipation(nd));
		}
		toMake.setParticipations(participations);
	}

	private static void populateAnnotations(IdentifiableDocument.Properties<QName> props, Identified toMake, Set<QName> toExclude) {
		final List<Annotation> annotations = new ArrayList<>();
		for (NamedProperty<QName> np : props.excluding(toExclude))
		{
			annotations.add(new Annotation(np));
		}
		toMake.setAnnotations(annotations);
	}

	private static Interaction parseInteraction(NestedDocument<QName> doc)
	{
		final IdentifiableDocument.Properties<QName> properties = doc.properties();
		Interaction toMake = new Interaction(doc.getIdentity());

		populateIdentified(properties, toMake);
		populateDocumented(properties, toMake);
		populateInteraction(properties, toMake);
		populateAnnotations(properties, toMake, Sbol2Terms.Interaction.terms);

		return toMake;
	}

	private static final void populateParticipation(IdentifiableDocument.Properties<QName> props, Participation toMake) {
		toMake.setParticipant(props.uri().getValue(Sbol2Terms.Participation.hasParticipant));

		final Set<URI> roles = new HashSet<>();
		for(URI role : props.uri().getValues(Sbol2Terms.Participation.role)) {
			roles.add(role);
		}
		toMake.setRoles(roles);
	}

	private static Participation parseParticipation(NestedDocument<QName> doc)
	{
		final IdentifiableDocument.Properties<QName> properties = doc.properties();
		final Participation toMake = new Participation(doc.getIdentity());

		populateIdentified(properties, toMake);
		populateParticipation(properties, toMake);
		populateAnnotations(properties, toMake, Sbol2Terms.Participation.all);

		return toMake;
	}

	private static final void populateComponentInstance(
			IdentifiableDocument.Properties<QName> props, ComponentInstance toMake)
	{
		toMake.setAccess(props.uri().getValue(Sbol2Terms.ComponentInstance.access));
		toMake.setDefinition(props.uri().getValue(Sbol2Terms.ComponentInstance.hasComponentDefinition));

		List<MapsTo> mapsTos = new ArrayList<>();
		for(NestedDocument<QName> doc : props.nestedDocument().getValues(Sbol2Terms.ComponentInstance.hasMapsTo)) {
			mapsTos.add(parseMapsTo(doc));
		}
		toMake.setMapsTo(mapsTos);
	}

	private static final void populateFunctionalComponent(
			IdentifiableDocument.Properties<QName> props, FunctionalComponent toMake)
	{
		toMake.setDirection(props.uri().getValue(Sbol2Terms.FunctionalComponent.direction));
	}

	private static final FunctionalComponent parseFunctionalComponent(NestedDocument<QName> doc) {
		final IdentifiableDocument.Properties<QName> properties = doc.properties();
		final FunctionalComponent toMake = new FunctionalComponent(doc.getIdentity());

		populateIdentified(properties, toMake);
		populateDocumented(properties, toMake);
		populateComponentInstance(properties, toMake);
		populateFunctionalComponent(properties, toMake);
		populateAnnotations(properties, toMake, Sbol2Terms.FunctionalComponent.all);

		return toMake;
	}


	private static Sequence parseSequences(SBOLDocument SBOLDoc, TopLevelDocument<QName> topLevel)
	{
		URI persistentIdentity = null;
		String version 		   = null;
		String displayId 	   = null;
		String name 		   = null;
		String description 	   = null;
		String elements 	   = null;
		URI encoding 		   = null;
		URI wasDerivedFrom 	   = null;
		List<Annotation> annotations = new ArrayList<>();

		for (NamedProperty<QName> namedProperty : topLevel.getProperties())
		{
			if (namedProperty.getName().equals(Sbol2Terms.Identified.persistentIdentity))
			{
				persistentIdentity = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Identified.version))
			{
				version  = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.displayId))
			{
				displayId = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Sequence.elements))
			{
				elements = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Sequence.encoding))
			{
				encoding = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.title))
			{
				name = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(Sbol2Terms.Documented.description))
			{
				description = ((Literal<QName>) namedProperty.getValue()).getValue().toString();
			}
			else if (namedProperty.getName().equals(ProvTerms.Prov.wasDerivedFrom))
			{
				wasDerivedFrom = URI.create(((Literal<QName>) namedProperty.getValue()).getValue().toString());
			}
			else
			{
				annotations.add(new Annotation(namedProperty));
			}
		}

		Sequence sequence = SBOLDoc.createSequence(topLevel.getIdentity(),
				elements, encoding);
		if (persistentIdentity != null)
			sequence.setPersistentIdentity(persistentIdentity);
		if (version != null)
			sequence.setVersion(version);
		if (displayId != null)
			sequence.setDisplayId(displayId);
		if (name != null)
			sequence.setName(name);
		if (description != null)
			sequence.setDescription(description);
		if (wasDerivedFrom != null)
			sequence.setWasDerivedFrom(wasDerivedFrom);
		if (!annotations.isEmpty())
			sequence.setAnnotations(annotations);
		return sequence;
	}

	/*private static Timestamp getTimestamp(String timeStamp)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		java.util.Date date  = null;
		try
		{
			date = sdf.parse(timeStamp);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		return timestamp;
	}*/

	/*
	private static URI getParentURI(URI identity)
	{
		String regex       = ".*[/]\\d+[/]\\d+";
		String regex_minor = ".*[/]\\d+[/]";
		String regex_major = ".*[/]\\d+";
		String regex_end   = ".*[/]";

		String identity_str = identity.toString();

		while (identity_str.matches(regex)
				|| identity_str.matches(regex_minor)
				|| identity_str.matches(regex_major)
				|| identity_str.matches(regex_end))
		{
			identity_str = identity_str.substring(0, identity_str.length() - 1);
		}

		return URI.create(identity_str);
	}
	*/
}
