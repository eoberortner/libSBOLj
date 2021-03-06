/*
 * Copyright (c) 2012 Clark & Parsia, LLC. <http://www.clarkparsia.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sbolstandard.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * The generic interface to read an SBOL document from an input stream.
 * 
 * @author Evren Sirin
 */
public interface SBOLReader {
	/**
	 * Reads the contents of an SBOL document from the given input stream into a newly created document instance.
	 * 
	 * @throws IOException if an IO error occurs while reading the contents of this document
	 * @throws SBOLValidationException if a validation error occurs while reading the contents of the document
	 */
	SBOLDocument read(InputStream in) throws IOException, SBOLValidationException;
}
