package org.sbolstandard.core2;

import java.net.URI;

/**
 * @author Zhen Zhang
 * @author Tramy Nguyen
 * @author Nicholas Roehner
 * @author Matthew Pocock
 * @author Goksel Misirli
 * @author Chris Myers
 * @version 2.0-beta
 */

public abstract class TopLevel extends Identified {
	
	public static final String COLLECTION = "col";
	public static final String MODULEDEFINITION = "md";
	public static final String MODEL = "mod";
	public static final String COMPONENTDEFINITION = "cd";
	public static final String SEQUENCE = "seq";
	public static final String GENERICTOPLEVEL = "gen";
		
	TopLevel(URI identity) {
		super(identity);
	}

	protected TopLevel(TopLevel toplevel) {
		super(toplevel);
	}
	
	protected abstract Identified deepCopy();
	
	/**
	 * Make a copy of a top-level object whose URI and its descendants' URIs (children, grandchildren, etc) are all compliant. 
	 * It first makes a deep copy of this object, then updates its own identity URI and all of its descendants' identity URIs
	 * according to the given {@code URIprefix, displayId}, and {@code version}. This method also updates the {@code displayId}
	 * and {@code version} fields for each updated object.
	 * @return the copied top-level object if this object and all of its descendants have compliant URIs, and {@code null} otherwise.
	 */
	abstract Identified copy(String URIprefix, String displayId, String version);
	
	/**
	 * Check if this top-level object's and all of its descendants' URIs are all compliant. 
	 * @return {@code true} if they are all compliant, {@code false} otherwise.
	 */
	protected abstract boolean checkDescendantsURIcompliance();

}
