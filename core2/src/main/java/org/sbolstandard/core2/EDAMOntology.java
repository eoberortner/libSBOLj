package org.sbolstandard.core2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
//import java.util.HashMap;
//import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.oboparser.obo.OBOOntology;
import org.oboparser.obo.OBOParser;
import org.oboparser.obo.OBOStanza;

/**
 * @author Zhen Zhang
 * @author Tramy Nguyen
 * @author Nicholas Roehner
 * @author Matthew Pocock
 * @author Goksel Misirli
 * @author Chris Myers
 * @version 2.0-beta
 */

public class EDAMOntology {
	private static final String URI_PREFIX = "http://identifiers.org/edam/";

	/**
	 * Namespace of the EDAM Ontology (<a href="http://identifiers.org/edam/">http://identifiers.org/edam/</a>).
	 */
	public static final URI NAMESPACE = URI.create(URI_PREFIX);
	private static OBOOntology EDAMOntology = null;
	
	// TODO: add FORMAT, SBML, CELLML, BIOPAX constants

	EDAMOntology() {
		OBOParser oboParser = new OBOParser();
		if (EDAMOntology == null) {
			InputStreamReader f = new InputStreamReader(getClass().
					getResourceAsStream("/ontologies/EDAMOntology/EDAM.obo"));
			try {
				oboParser.parse(f);
				EDAMOntology = oboParser.getOntology();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the extracted ID of the given stanza's URI. 
	 * 
	 * @param stanzaURI
	 * @return the extracted ID of the given stanza's URI.
	 */
	public final String getId(URI stanzaURI) {
		String stanzaURIstr = stanzaURI.toString().trim();
		if (!stanzaURIstr.startsWith(URI_PREFIX)) {
			try {
				throw new IllegalArgumentException("Illegal " + stanzaURI.toString() + ". It does not begin with the URI prefix " + URI_PREFIX);
			}
			catch (IllegalArgumentException e) {
				return null;
			}
		}
		int beginIndex = stanzaURIstr.lastIndexOf("/") + 1;
		return stanzaURIstr.substring(beginIndex, stanzaURIstr.length());
	}

	/**
	 * Returns the ID field of the stanza whose name matches the given name. If multiple matches are found, only the first matching
	 * one is returned.
	 *  
	 * @param stanzaName
	 * @return the ID the matching stanza, or {@code null} if no match is found.
	 */
	public final String getId(String stanzaName) {
		List<String> IdList = new ArrayList<String>();	
		for (OBOStanza stanza : EDAMOntology.getStanzas()) {
			if (stanzaName.trim().equals(stanza.getName().trim())) {
				IdList.add(stanza.getId());
			}
		}
		if (IdList.isEmpty()) {
			try {
				throw new IllegalArgumentException("Illegal name " + stanzaName + ". It does not exist.");
			}
			catch (IllegalArgumentException e) {
				return null;
			}
		}
		return IdList.get(0);
	}


	/**
	 * Returns the name field of the stanza that matches the ID for the given stanzaURI.
	 * 
	 * @param stanzaURI
	 * @return the name field of the stanza that matches the ID in the given stanzaURI.
	 */
	public final String getName(URI stanzaURI) {
		String oboURIstr = stanzaURI.toString().trim();
		if (!oboURIstr.startsWith(URI_PREFIX)) {
			try {
				throw new IllegalArgumentException("Illegal " + stanzaURI.toString() + ". It does not contain URI prefix " + URI_PREFIX);
			}
			catch (IllegalArgumentException e) {
				return null;
			}
		}
		int beginIndex = oboURIstr.lastIndexOf("/") + 1;
		String id = oboURIstr.substring(beginIndex, oboURIstr.length());
		OBOStanza oboStanza = EDAMOntology.getStanza(id);
		if (oboStanza == null) {
			try {
				throw new IllegalArgumentException("ID " + id + " does not exist.");
			}
			catch (IllegalArgumentException e) {
				return null;
			}
		}
		return oboStanza.getName();
	}

	/**
	 * Returns the name field of the stanza that matches the ID in the given stanzaURI.
	 * 
	 * @param stanzaId
	 * @return the name field of the stanza that matches the ID in the given stanzaURI,
					or {@code null} if this no match is found.
	 */
	public final String getName(String stanzaId) {
		OBOStanza oboStanza = EDAMOntology.getStanza(stanzaId);
		if (oboStanza == null) {
			try {
				throw new IllegalArgumentException("Illegal ID " + stanzaId + " does not exist.");
			}
			catch (IllegalArgumentException e) {
				return null;
			}
		}
		return oboStanza.getName();
	}

	/**
	 * Returns the URI, i.e. the EDAM Ontology namespace URL followed by an ID of an sequence ontology term, 
	 * of the stanza whose name matches the given name. If multiple matches are found, only the first matching
	 * one is returned. 
	 * 
	 * @param stanzaName
	 * @return the URI of the given EDAM ontology name.
	 */
	public final URI getURIbyName(String stanzaName) {
		return getURIbyId(getId(stanzaName));
	}

	/** 
	 * Creates a new URI from the EDAM Ontology namespace with the given ID. 
	 * @param stanzaId
	 * @return the created URI
	 */
	public final URI getURIbyId(String stanzaId) {
		if (stanzaId==null) return null;
		OBOStanza oboStanza = EDAMOntology.getStanza(stanzaId.trim());
		if (oboStanza == null) {
			try {
				throw new IllegalArgumentException("ID " + stanzaId + " does not exist.");
			}
			catch (IllegalArgumentException e) {
				return null;
			}
		}
		return URI.create(URI_PREFIX+stanzaId);
	}

	/**
	 * Returns {@code true} if the stanza with Id1 is a descendant of the stanza with Id2.  
	 * @param Id1
	 * @param Id2
	 * @return {@code true} if the stanza with Id1 is a descendant of the stanza with Id2, {@code false} otherwise.
	 */
	public boolean isDescendantOf(String Id1, String Id2) {
		OBOStanza stanza1 = EDAMOntology.getStanza(Id1);
		OBOStanza stanza2 = EDAMOntology.getStanza(Id2);
		if (stanza1 == null) {
			try {
				throw new IllegalArgumentException("Illegal ID: " + Id1 + ". No match was found.");
			}
			catch (IllegalArgumentException e) {
				return false;
			}
		}
		if (stanza2 == null) {
			try {
				throw new IllegalArgumentException("Illegal ID: " + Id2 + ". No match was found.");
			}
			catch (IllegalArgumentException e) {
				return false;
			}
		}
		return EDAMOntology.isDescendantOf(stanza1, stanza2);
	}

	/**
	 * Returns {@code true} if the stanza with Id1 is a descendant of the stanza with Id2.  
	 * @param childURI
	 * @param parentURI
	 * @return {@code true} if the stanza with Id1 is a descendant of the stanza with Id2, {@code false} otherwise.
	 */
	public final boolean isDescendantOf(URI childURI, URI parentURI) {
		String childId = getId(childURI);
		String parentId = getId(parentURI);
		return isDescendantOf(childId,parentId);
	}
	
	/**
	 * Creates a new URI from the EDAM Ontology namespace with the given local name. For example, the function call
	 * <code>term("format_1915")</code> will return the URI <a>http://purl.obolibrary.org/edam/format_1915</a>
	 * @param localName 
	 * @return the created URI
	 */
	public static final URI type(String localName) {
		return URI.create(URI_PREFIX+localName);
	}
	
	/**
	 * A defined way or layout of representing and structuring data in a computer file, blob, 
	 * string, message, or elsewhere. The main focus in EDAM lies on formats as means of 
	 * structuring data exchanged between different tools or resources. The serialisation, 
	 * compression, or encoding of concrete data formats/models is not in scope of EDAM. 
	 * Format 'is format of' Data.
	 * (<a href="http://identifiers.org/edam/format_1915">FORMAT</a>).
	 */
	public static final URI FORMAT = type("format_1915");
	//public static final URI FORMAT = URI.create("http://identifiers.org/edam/format_1915");
	
	/**
	 * Systems Biology Markup Language (SBML), the standard XML format for models of biological
	 * processes such as for example metabolism, cell signaling, and gene regulation
	 * (<a href="http://identifiers.org/edam/format_2585">SBML</a>).
	 */
	public static final URI SBML = type("format_2585");
	//public static final URI SBML = URI.create("http://identifiers.org/edam/format_2585");

	/**
	 * CellML, the format for mathematical models of biological and other networks
	 * (<a href="http://identifiers.org/edam/format_3240">CELLML</a>).
	 */
	public static final URI CELLML = type("format_3240");

	/**
	 * BioPAX is an exchange format for pathway data, with its data model defined in OWL
	 * (<a href="http://identifiers.org/edam/format_3156">BIOPAX</a>).
	 */
	public static final URI BIOPAX = type("format_3156");
}
