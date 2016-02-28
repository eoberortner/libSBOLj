package org.sbolstandard.core2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import uk.ac.ncl.intbio.core.io.CoreIoException;

public final class SBOLFactory {
	
	private static SBOLDocument document = new SBOLDocument();

	/**
	 * This sets the internal SBOLDocument used by the factory.
	 * @param sbolDocument
	 */
	public static void clear() {
		document = new SBOLDocument();
	}
	
	/**
	 * Creates a ModuleDefinition instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given arguments, and an empty version string, and then
	 * adds it to this SBOLDocument object's list of ModuleDefinition instances.
	 * <p>
	 * This method calls {@link #createModuleDefinition(String, String, String)} to do the following
	 * validity checks and create a ModuleDefinition instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#",
	 * then "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} is not {@code null} and is valid.
	 * <p>
	 * A ModuleDefinition instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#MODULE_DEFINITION},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @return the created ModuleDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ModuleDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ModuleDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ModuleDefinition instances.
	 */
	public static ModuleDefinition createModuleDefinition(String displayId) throws SBOLValidationException {
		return document.createModuleDefinition(displayId);
	}

	/**
	 * Creates a ModuleDefinition instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to this SBOLDocument object's list of ModuleDefinition instances.
	 * <p>
	 * This method calls {@link #createModuleDefinition(String, String, String)} to do the following
	 * validity checks and create a ModuleDefinition instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} and {@code version} arguments are not {@code null}
	 * and are both valid.
	 * <p>
	 * A ModuleDefinition instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#MODULE_DEFINITION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param version
	 * @return the created ModuleDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ModuleDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ModuleDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ModuleDefinition instances.
	 */
	public static ModuleDefinition createModuleDefinition(String displayId, String version) throws SBOLValidationException {
		return document.createModuleDefinition(displayId,version);
	}

	/**
	 * Creates a ModuleDefinition instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of ModuleDefinition instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires that the given {@code URIprefix}, {@code displayId},
	 * and {@code version} are not {@code null} and are valid.
	 * <p>
	 * A ModuleDefinition instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#MODULE_DEFINITION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @return the created ModuleDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ModuleDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ModuleDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ModuleDefinition instances.
	 */
	public static ModuleDefinition createModuleDefinition(String URIprefix,String displayId, String version) throws SBOLValidationException {
		return document.createModuleDefinition(URIprefix, displayId, version);
	}

	/**
	 * Removes the given {@code moduleDefinition} from this SBOLDocument object's list of ModuleDefinition instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param moduleDefinition
	 * @return {@code true} if the given {@code moduleDefinition} is successfully removed, {@code false} otherwise.
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and one of its ModuleDefinition instances has a Module instance that refers to the given
	 * {@code moduleDefinition} (see {@link Module#getDefinitionURI()}).
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code moduleDefinition} is referenced by any of its Collection instances as a member.
	 */
	public static boolean removeModuleDefinition(ModuleDefinition moduleDefinition) throws SBOLValidationException {
		return document.removeModuleDefinition(moduleDefinition);
	}

	/**
	 * Returns the ModuleDefinition instance matching the given {@code displayId}
	 * and {@code version} from this SBOLDocument object's list of
	 * ModuleDefinition instances.
	 * <p>
	 * A compliant ModuleDefinition URI is created first using the {@code defaultURIprefix},
	 * the optional type {@link TopLevel#MODULE_DEFINITION}, the given {@code displayId}
	 * and {@code version}. This URI is used to look up the ModuleDefinition instance
	 * in this SBOLDocument object.
	 *
	 * @param displayId
	 * @param version
	 * @return the matching ModuleDefinition instance if present, or {@code null} otherwise.
	 */
	public static ModuleDefinition getModuleDefinition(String displayId,String version) {
		return document.getModuleDefinition(displayId, version);
	}

	/**
	 * Returns the ModuleDefinition instance matching the given {@code modelURI} from this
	 * SBOLDocument object's list of ModuleDefinition instances.
	 *
	 * @param moduleURI
	 * @return the matching ModuleDefinition instance if present, or {@code null} otherwise.
	 */
	public static ModuleDefinition getModuleDefinition(URI moduleURI) {
		return document.getModuleDefinition(moduleURI);
	}

	/**
	 * Returns the set of {@code ModuleDefinition} instances owned by this SBOLDocument object.
	 *
	 * @return the set of {@code ModuleDefinition} instances owned by this SBOLDocument object.
	 */
	public static Set<ModuleDefinition> getModuleDefinitions() {
		return document.getModuleDefinitions();
	}

	/**
	 * Removes all entries in the list of ModuleDefinition instances
	 * owned by this SBOLDocument object. The list will be empty after this call returns.
	 * @throws SBOLValidationException 
	 */
	public static void clearModuleDefinitions() throws SBOLValidationException {
		document.clearModuleDefinitions();
	}

	/**
	 * Creates a Collection instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given {@code displayId} argument, and an empty version string, and then
	 * adds it to this SBOLDocument object's list of Collection instances.
	 * <p>
	 * This method calls {@link #createCollection(String, String, String)} to do the following
	 * validity checks and create a Collection instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} argument is not {@code null} and is valid.
	 * <p>
	 * A Collection instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#COLLECTION},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @return the created Collection instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the defaultURIprefix is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Collection instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Collection instance's identity URI
	 * exists in this SBOLDocument object's list of Collection instances.
	 */
	public static Collection createCollection(String displayId) throws SBOLValidationException {
		return document.createCollection(displayId);
	}

	/**
	 * Creates a Collection instance with this SBOLDocument object's {@code defaultURIprefix} and
	 * the given arguments, and then adds it to this SBOLDocument object's list of Collection instances.
	 * <p>
	 * This method calls {@link #createCollection(String, String, String)} to do the following
	 * validity checks and create a Collection instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code URIprefix}, {@code displayId}, and {@code version} are not
	 * {@code null} and valid.
	 * <p>
	 * A Collection instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#COLLECTION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param version
	 * @return the created Collection instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the defaultURIprefix is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Collection instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Collection instance's identity URI
	 * exists in this SBOLDocument object's list of Collection instances.
	 */
	public static Collection createCollection(String displayId, String version) throws SBOLValidationException {
		return document.createCollection(displayId, version);
	}

	/**
	 * Creates a Collection instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of Collection instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code URIprefix}, {@code displayId}, and {@code version} are not
	 * {@code null} and valid.
	 * <p>
	 * A Collection instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#COLLECTION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @return the created Collection instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the defaultURIprefix is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Collection instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Collection instance's identity URI
	 * exists in this SBOLDocument object's list of Collection instances.
	 */
	public static Collection createCollection(String URIprefix, String displayId, String version) throws SBOLValidationException {
		return document.createCollection(URIprefix, displayId, version);
	}

	/**
	 * Removes the given {@code collection} from this SBOLDocument object's list of Collection instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param collection
	 * @return {@code true} if the given {@code collection} is successfully removed, {@code false} otherwise.
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code collection} is referenced by any of its Collection instances as a member.
	 */
	public static boolean removeCollection(Collection collection) throws SBOLValidationException {
		return document.removeCollection(collection);
	}

	/**
	 * Returns the Collection instance matching the given {@code displayId}
	 * and {@code version} from this SBOLDocument object's list of
	 * Collection instances.
	 * <p>
	 * A compliant Collection URI is created first using the {@code defaultURIprefix},
	 * the optional type {@link TopLevel#COLLECTION}, the given {@code displayId}
	 * and {@code version}. This URI is used to look up the Collection instance
	 * in this SBOLDocument object.
	 *
	 * @param displayId
	 * @param version
	 * @return the matching Collection instance if present, or {@code null} otherwise.
	 */
	public static Collection getCollection(String displayId,String version) {
		return document.getCollection(displayId, version);
	}

	/**
	 * Returns the Collection instance matching the given {@code collectionURI} from this
	 * SBOLDocument object's list of Collection instances.
	 *
	 * @param collectionURI
	 * @return the matching Collection instance if present, or {@code null} otherwise.
	 *
	 */
	public static Collection getCollection(URI collectionURI) {
		return document.getCollection(collectionURI);
	}

	/**
	 * Returns the set of {@code Collection} instances owned by this SBOLDocument object.
	 *
	 * @return the set of {@code Collection} instances owned by this SBOLDocument object.
	 */
	public static Set<Collection> getCollections() {
		return document.getCollections();
	}

	/**
	 * Removes all entries in the list of Collection instances
	 * owned by this SBOLDocument object. The list will be empty after this call returns.
	 * @throws SBOLValidationException 
	 */
	public static void clearCollections() throws SBOLValidationException {
		document.clearCollections();
	}

	/**
	 * Creates a Model instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given arguments, and an empty version string, and then
	 * adds it to this SBOLDocument object's list of Model instances.
	 * <p>
	 * This method calls {@link #createModel(String, String, String, URI, URI, URI)} to do the following
	 * validity checks and create a Model instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#",
	 * then "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} is not {@code null} and is valid.
	 * <p>
	 * A Model instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#MODEL},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param source
	 * @param language
	 * @param framework
	 * @return the created Model instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Model instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Model instance's identity URI
	 * exists in this SBOLDocument object's list of Model instances.
	 */
	public static Model createModel(String displayId, URI source, URI language, URI framework) throws SBOLValidationException {
		return document.createModel(displayId,"",source,language,framework);
	}

	/**
	 * Creates a Model instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to this SBOLDocument object's list of Model instances.
	 * <p>
	 * This method calls {@link #createModel(String, String, String, URI, URI, URI)} to do the following
	 * validity checks and create a Model instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} and {@code version} arguments are not {@code null}
	 * and are both valid.
	 * <p>
	 * A Model instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#MODEL},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param version
	 * @param source
	 * @param language
	 * @param framework
	 * @return the created Model instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Model instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Model instance's identity URI
	 * exists in this SBOLDocument object's list of Model instances.
	 */
	public static Model createModel(String displayId, String version, URI source, URI language, URI framework) throws SBOLValidationException {
		return document.createModel(displayId,version,source,language,framework);
	}

	/**
	 * Creates a Model instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of Model instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires that the given {@code URIprefix}, {@code displayId},
	 * and {@code version} are not {@code null} and are valid.
	 * <p>
	 * A Model instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#MODEL},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @param source
	 * @param language
	 * @param framework
	 * @return the created Model instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Model instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Model instance's identity URI
	 * exists in this SBOLDocument object's list of Model instances.
	 */
	public static Model createModel(String URIprefix, String displayId, String version, URI source, URI language, URI framework) throws SBOLValidationException {
		return document.createModel(URIprefix,displayId,version,source,language,framework);
	}

	/**
	 * Removes the given {@code model} from this SBOLDocument object's list of Model instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param model
	 * @return {@code true} if the given {@code model} is successfully removed, {@code false} otherwise.
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and one of its ModuleDefinition instances refers to the given {@code model}
	 * (see {@link ModuleDefinition#containsModel(URI)}).
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code model} is referenced by any of its Collection instances as a member.
	 */
	public static boolean removeModel(Model model) throws SBOLValidationException {
		return document.removeModel(model);
	}

	/**
	 * Returns the Model instance matching the given {@code displayId}
	 * and {@code version} from this SBOLDocument object's list of
	 * Model instances.
	 * <p>
	 * A compliant Model URI is created first using the {@code defaultURIprefix},
	 * the optional type {@link TopLevel#MODEL}, the given {@code displayId}
	 * and {@code version}. This URI is used to look up the Model instance
	 * in this SBOLDocument object.
	 *
	 * @param displayId
	 * @param version
	 * @return the matching Model instance if present, or {@code null} otherwise.
	 */
	public static Model getModel(String displayId,String version) {
		return document.getModel(displayId,version);
	}

	/**
	 * Returns the Model instance matching the given {@code modelURI} from this
	 * SBOLDocument object's list of Model instances.
	 *
	 * @param modelURI
	 * @return the matching Model instance if present, or {@code null} otherwise.
	 */
	public static Model getModel(URI modelURI) {
		return document.getModel(modelURI);
	}

	/**
	 * Returns the set of {@code Model} instances owned by this SBOLDocument object.
	 *
	 * @return the set of {@code Model} instances owned by this SBOLDocument object.
	 */
	public static Set<Model> getModels() {
		return document.getModels();
	}

	/**
	 * Removes all entries in the list of Model instances
	 * owned by this SBOLDocument object. The list will be empty after this call returns.
	 * @throws SBOLValidationException 
	 */
	public static void clearModels() throws SBOLValidationException {
		document.clearModels();
	}

	/**
	 * Creates a ComponentDefinition instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given arguments, and empty version string, and then
	 * adds it to this SBOLDocument object's list of ComponentDefinition instances.
	 * <p>
	 * This method calls {@link #createComponentDefinition(String, String, String, Set)} to do the following
	 * validity checks and create a ComponentDefinition instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#",
	 * then "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} is not {@code null} and is valid.
	 * <p>
	 * A ComponentDefinition instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#COMPONENT_DEFINITION},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param types
	 * @return the created ComponentDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ComponentDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ComponentDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ComponentDefinition instances.
	 */
	public static ComponentDefinition createComponentDefinition(String displayId, Set<URI> types) throws SBOLValidationException {
		return document.createComponentDefinition(displayId, types);
	}
	

	/**
	 * Creates a ComponentDefinition instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given arguments, and empty version string, and then
	 * adds it to this SBOLDocument object's list of ComponentDefinition instances.
	 * <p>
	 * This method calls {@link #createComponentDefinition(String, String, String, URI)} to do the following
	 * validity checks and create a ComponentDefinition instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#",
	 * then "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} is not {@code null} and is valid.
	 * <p>
	 * A ComponentDefinition instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#COMPONENT_DEFINITION},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param type
	 * @return the created ComponentDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ComponentDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ComponentDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ComponentDefinition instances.
	 */
	public static ComponentDefinition createComponentDefinition(String displayId, URI type) throws SBOLValidationException {
		return document.createComponentDefinition(displayId, type);
	}

	/**
	 * Creates a ComponentDefinition instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to this SBOLDocument object's list of ComponentDefinition instances.
	 * <p>
	 * This method calls {@link #createComponentDefinition(String, String, String, Set)} to do the following
	 * validity checks and create a ComponentDefinition instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} and {@code version} arguments are not {@code null}
	 * and are both valid.
	 * <p>
	 * A ComponentDefinition instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#COMPONENT_DEFINITION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param version
	 * @param types
	 * @return the created ComponentDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ComponentDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ComponentDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ComponentDefinition instances.
	 */
	public static ComponentDefinition createComponentDefinition(String displayId, String version, Set<URI> types) throws SBOLValidationException {
		return document.createComponentDefinition(displayId, version, types);
	}
	
	/**
	 * Creates a ComponentDefinition instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to this SBOLDocument object's list of ComponentDefinition instances.
	 * <p>
	 * This method calls {@link #createComponentDefinition(String, String, String, URI)} to do the following
	 * validity checks and create a ComponentDefinition instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} and {@code version} arguments are not {@code null}
	 * and are both valid.
	 * <p>
	 * A ComponentDefinition instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#COMPONENT_DEFINITION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param version
	 * @param type
	 * @return the created ComponentDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ComponentDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ComponentDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ComponentDefinition instances.
	 */
	public static ComponentDefinition createComponentDefinition(String displayId, String version, URI type) throws SBOLValidationException {
		return document.createComponentDefinition(displayId, version, type);
	}

	/**
	 * Creates a ComponentDefinition instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of ComponentDefinition instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires that the given {@code URIprefix}, {@code displayId},
	 * and {@code version} are not {@code null} and are valid.
	 * <p>
	 * A ComponentDefinition instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#COMPONENT_DEFINITION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @param types
	 * @return the created ComponentDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ComponentDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ComponentDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ComponentDefinition instances.
	 */
	public static ComponentDefinition createComponentDefinition(String URIprefix,String displayId, String version, Set<URI> types) throws SBOLValidationException {
		return document.createComponentDefinition(URIprefix, displayId, version, types);
	}

	/**
	 * Creates a ComponentDefinition instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of ComponentDefinition instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires that the given {@code URIprefix}, {@code displayId},
	 * and {@code version} are not {@code null} and are valid.
	 * <p>
	 * A ComponentDefinition instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#COMPONENT_DEFINITION},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @param type
	 * @return the created ComponentDefinition instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created ComponentDefinition instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created ComponentDefinition instance's identity URI
	 * exists in this SBOLDocument object's list of ComponentDefinition instances.
	 */
	public static ComponentDefinition createComponentDefinition(String URIprefix,String displayId, String version, URI type) throws SBOLValidationException {
		return document.createComponentDefinition(URIprefix, displayId, version, type);
	}

	/**
	 * Removes the given {@code componentDefinition} from this SBOLDocument object's list of ComponentDefinition instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param componentDefinition
	 * @return {@code true} if the given {@code componentDefinition} is successfully removed, {@code false} otherwise.
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code componentDefinition} is referenced by any of its ComponentDefinition instances.
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and one of its ModuleDefinition instances owns a FunctionalComponent instance that
	 * refers to the given {@code componentDefinition} as its {@code definition}
	 * (see {@link FunctionalComponent#getDefinitionURI()}).
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code componentDefinition} is referenced by any of its Collection instances as a member.
	 */
	public static boolean removeComponentDefinition(ComponentDefinition componentDefinition) throws SBOLValidationException {
		return document.removeComponentDefinition(componentDefinition);
	}

	/**
	 * Returns the ComponentDefinition instance matching the given {@code displayId}
	 * and {@code version} from this SBOLDocument object's list of
	 * ComponentDefinition instances.
	 * <p>
	 * A compliant ComponentDefinition URI is created first using the {@code defaultURIprefix},
	 * the optional type {@link TopLevel#COMPONENT_DEFINITION}, the given {@code displayId}
	 * and {@code version}. This URI is used to look up the ComponentDefinition instance
	 * in this SBOLDocument object.
	 *
	 * @param displayId
	 * @param version
	 * @return the matching ComponentDefinition instance if present, or {@code null} otherwise.
	 */
	public static ComponentDefinition getComponentDefinition(String displayId,String version) {
		return document.getComponentDefinition(displayId, version);
	}

	/**
	 * Returns the ComponentDefinition instance matching the given {@code componentDefinitionURI} from this
	 * SBOLDocument object's list of ComponentDefinition instances.
	 *
	 * @param componentDefinitionURI
	 * @return the matching ComponentDefinition instance if present, or {@code null} otherwise.
	 */
	public static ComponentDefinition getComponentDefinition(URI componentDefinitionURI) {
		return document.getComponentDefinition(componentDefinitionURI);
	}

	/**
	 * Returns the set of {@code ComponentDefinition} instances owned by this SBOLDocument object.
	 *
	 * @return the set of {@code ComponentDefinition} instances owned by this SBOLDocument object.
	 */
	public static Set<ComponentDefinition> getComponentDefinitions() {
		return document.getComponentDefinitions();
	}
	
	public static Set<ComponentDefinition> getRootComponentDefinitions() {
		return document.getRootComponentDefinitions();
	}

	/**
	 * Removes all entries in the list of ComponentDefinition instances
	 * owned by this SBOLDocument object. The list will be empty after this call returns.
	 * @throws SBOLValidationException 
	 */
	public static void clearComponentDefinitions() throws SBOLValidationException {
		document.clearComponentDefinitions();
	}

	/**
	 * Creates a Sequence instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given arguments, and an empty version string, and then
	 * adds it to this SBOLDocument object's list of Sequence instances.
	 * <p>
	 * This method calls {@link #createSequence(String, String, String, String, URI)} to do the following
	 * validity checks and create a Sequence instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#",
	 * then "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} is not {@code null} and is valid.
	 * <p>
	 * A Sequence instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#SEQUENCE},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param elements
	 * @param encoding
	 * @return the created Sequence instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Sequence instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Sequence instance's identity URI
	 * exists in this SBOLDocument object's list of Sequence instances.
	 */
	public static Sequence createSequence(String displayId, String elements, URI encoding) throws SBOLValidationException {
		return document.createSequence(displayId,elements,encoding);
	}

	/**
	 * Creates a Sequence instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to this SBOLDocument object's list of Sequence instances.
	 * <p>
	 * This method calls {@link #createSequence(String, String, String, String, URI)} to do the following
	 * validity checks and create a Sequence instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} and {@code version} arguments are not {@code null}
	 * and are both valid.
	 * <p>
	 * A Sequence instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#SEQUENCE},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 *
	 * @param displayId
	 * @param version
	 * @param elements
	 * @param encoding
	 * @return the created Sequence instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Sequence instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Sequence instance's identity URI
	 * exists in this SBOLDocument object's list of Sequence instances.
	 */
	public static Sequence createSequence(String displayId, String version, String elements, URI encoding) throws SBOLValidationException {
		return document.createSequence(displayId,version,elements,encoding);
	}

	/**
	 * Creates a Sequence instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of Sequence instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires that the given {@code URIprefix}, {@code displayId},
	 * and {@code version} are not {@code null} and are valid.
	 * <p>
	 * A Sequence instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#SEQUENCE},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @param elements
	 * @param encoding
	 * @return the created Sequence instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created Sequence instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created Sequence instance's identity URI
	 * exists in this SBOLDocument object's list of Sequence instances.
	 */
	public static Sequence createSequence(String URIprefix, String displayId, String version, String elements, URI encoding) throws SBOLValidationException {
		return document.createSequence(URIprefix, displayId, version, elements, encoding);
	}

	//	/**
	// 	 * Create a copy of the given top-level object, i.e.{@link Collection}, {@link ComponentDefinition}, {@link Model}, {@link ModuleDefinition},
	//	 * {@link Sequence}, or {@link TopLevel} with the given version, and add it to its corresponding top-level objects list.
	//	 * @param toplevel
	//	 * @param newURIprefix
	//	 * @return the created {@link TopLevel} object
	//	 */
	//	public TopLevel createCopyWithURIprefix(TopLevel toplevel, String newURIprefix) {
	//		String olddisplayId = extractDisplayId(((Collection) toplevel).getIdentity(), 0);
	//		String oldVersion = extractVersion(toplevel.getIdentity());
	//		return createCopy(toplevel, newURIprefix, olddisplayId, oldVersion);
	//	}
	//
	//	/**
	//	 * Create a copy of the given top-level object, i.e.{@link Collection}, {@link ComponentDefinition}, {@link Model}, {@link ModuleDefinition},
	//	 * {@link Sequence}, or {@link TopLevel} with the given version, and add it to its corresponding top-level objects list.
	//	 * @param toplevel
	//	 * @param newVersion
	//	 * @return {@link TopLevel} object
	//	 */
	//	public TopLevel createCopyWithVersion(TopLevel toplevel, String newVersion) {
	//		String oldURIprefix = extractURIprefix(((Collection) toplevel).getIdentity());
	//		String olddisplayId = extractDisplayId(((Collection) toplevel).getIdentity(), 0);
	//		return createCopy(toplevel, oldURIprefix, olddisplayId, newVersion);
	//	}
	//
	//	/**
	//	 * Create a copy of the given top-level object, which is i.e.{@link Collection}, {@link ComponentDefinition}, {@link Model}, {@link ModuleDefinition},
	//	 * {@link Sequence}, or {@link GenericTopLevel} with the given display ID, and add it to its corresponding top-level objects list.
	//	 * @param toplevel
	//	 * @param newDisplayId
	//	 * @return {@link TopLevel} object
	//	 */
	//	public TopLevel createCopyWithDisplayId(TopLevel toplevel, String newDisplayId) {
	//		String oldURIprefix = extractURIprefix(toplevel.getIdentity());
	//		String oldVersion = extractVersion(toplevel.getIdentity());
	//		return createCopy(toplevel, oldURIprefix,
	//				newDisplayId, oldVersion);
	//	}
	//
	//	/**
	//	 * Create a copy of the given top-level object, which is i.e.{@link Collection}, {@link ComponentDefinition}, {@link Model}, {@link ModuleDefinition},
	//	 * {@link Sequence}, or {@link GenericTopLevel} with the given URIprefix and display ID, and add it to its corresponding top-level objects list.
	//	 * @param toplevel
	//	 * @param newDisplayId
	//	 * @return {@link TopLevel} object
	//	 */
	//	public TopLevel createCopyWithPersistentId(TopLevel toplevel, String newURIprefix, String newDisplayId) {
	//		String oldVersion = extractVersion(toplevel.getIdentity());
	//		return createCopy(toplevel, newURIprefix,
	//				newDisplayId, oldVersion);
	//	}

	//	/**
	//	 * Create an object of the top-level classes, i.e.{@link Collection}, {@link ComponentDefinition}, {@link Model}, {@link ModuleDefinition},
	//	 * {@link Sequence}, or {@link TopLevel} with a new display ID, and add it to its corresponding top-level objects list.
	//	 * @param toplevel
	//	 * @param newPrefix
	//	 * @return {@link TopLevel} object
	//	 */
	//	public TopLevel createCopyWithNewPrefix(TopLevel toplevel, String newPrefix) {
	//		if (toplevel objectof Collection) {
	//			Collection newCollection = ((Collection) toplevel).copy(newPrefix);
	//			if (addCollection(newCollection)) {
	//				return newCollection;
	//			}
	//			else {
	//				return null;
	//			}
	//		}
	//		else if (toplevel objectof ComponentDefinition) {
	//			ComponentDefinition newComponentDefinition = ((ComponentDefinition) toplevel).copy(newPrefix);
	//			if (addComponentDefinition(newComponentDefinition)) {
	//				return newComponentDefinition;
	//			}
	//			else {
	//				return null;
	//			}
	//		}
	//		else if (toplevel objectof Model) {
	//			Model newModel = ((Model) toplevel).copy(newPrefix);
	//			if (addModel(newModel)) {
	//				return newModel;
	//			}
	//			else {
	//				return null;
	//			}
	//		}
	//		else if (toplevel objectof ModuleDefinition) {
	//			ModuleDefinition newModuleDefinition = ((ModuleDefinition) toplevel).copy(newPrefix);
	//			if (addModuleDefinition(newModuleDefinition)) {
	//				return newModuleDefinition;
	//			}
	//			else {
	//				return null;
	//			}
	//		}
	//		else if (toplevel objectof Sequence) {
	//			Sequence newSequence = ((Sequence) toplevel).copy(newPrefix);
	//			if (addSequence(newSequence)) {
	//				return newSequence;
	//			}
	//			else {
	//				return null;
	//			}
	//		}
	//		else if (toplevel objectof GenericTopLevel) {
	//			GenericTopLevel newGenericTopLevel = ((GenericTopLevel) toplevel).copy(newPrefix);
	//			if (addGenericTopLevel(newGenericTopLevel)) {
	//				return newGenericTopLevel;
	//			}
	//			else {
	//				return null;
	//			}
	//		}
	//		else {
	//			return null;
	//		}
	//	}

	/**
	 * Creates an identical copy of the given TopLevel instance.
	 * <p>
	 * This method calls {@link #createCopy(TopLevel, String, String, String)} to do the following
	 * validity checks and create a copy top-level instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} is {@code null}, then it is extracted from the given
	 * {@code topLevel} instance. If it does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * If either the given {@code displayId} or {@code version}, then the corresponding field
	 * is extracted from the given {@code topLevel} instance. Both extracted fields are required
	 * to be valid and not {@code null}.
	 * <p>
	 * A top-level instance with a compliant URI using the given arguments,
	 * and then its display ID, persistent identity, and version fields are set. This
	 * instance is then added to the corresponding top-level list owned by this SBOLDocument object.
	 *
	 * @param topLevel
	 * @return the created top-level instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created top-level instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created top-level instance's identity URI
	 * already exists.
	 * @throws SBOLValidationException if the given {@code topLevel} instance is not an instance
	 * of a top-level object
	 */
	public static TopLevel createCopy(TopLevel topLevel) throws SBOLValidationException {
		return document.createCopy(topLevel);
	}

	/**
	 * Creates a copy of the given TopLevel instance with this SBOLDocument object's {@code defaultURIprefix}
	 * the given arguments, and an empty version string, and then adds it to the
	 * corresponding top-level list owned by this SBOLDocument object.
	 * <p>
	 * This method calls {@link #createCopy(TopLevel, String, String, String)} to do the following
	 * validity checks and create a copy top-level instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} is {@code null}, then it is extracted from the given
	 * {@code topLevel} instance. If it does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * If either the given {@code displayId} or {@code version}, then the corresponding field
	 * is extracted from the given {@code topLevel} instance. Both extracted fields are required
	 * to be valid and not {@code null}.
	 * <p>
	 * A top-level instance with a compliant URI using the given arguments,
	 * and then its display ID, persistent identity, and version fields are set. This
	 * instance is then added to the corresponding top-level list owned by this SBOLDocument object.
	 *
	 * @param topLevel
	 * @param displayId
	 * @return the created top-level instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created top-level instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created top-level instance's identity URI
	 * already exists.
	 * @throws SBOLValidationException if the given {@code topLevel} instance is not an instance
	 * of a top-level object
	 */
	public static TopLevel createCopy(TopLevel topLevel, String displayId) throws SBOLValidationException {
		return document.createCopy(topLevel,displayId);
	}

	/**
	 * Creates a copy of the given TopLevel instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to the
	 * corresponding top-level list owned by this SBOLDocument object.
	 * <p>
	 * This method calls {@link #createCopy(TopLevel, String, String, String)} to do the following
	 * validity checks and create a copy top-level instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} is {@code null}, then it is extracted from the given
	 * {@code topLevel} instance. If it does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * If either the given {@code displayId} or {@code version}, then the corresponding field
	 * is extracted from the given {@code topLevel} instance. Both extracted fields are required
	 * to be valid and not {@code null}.
	 * <p>
	 * A top-level instance with a compliant URI using the given arguments,
	 * and then its display ID, persistent identity, and version fields are set. This
	 * instance is then added to the corresponding top-level list owned by this SBOLDocument object.
	 *
	 * @param topLevel
	 * @param displayId
	 * @param version
	 * @return the created top-level instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created top-level instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created top-level instance's identity URI
	 * already exists.
	 * @throws SBOLValidationException if the given {@code topLevel} instance is not an instance
	 * of a top-level object
	 */
	public static TopLevel createCopy(TopLevel topLevel, String displayId, String version) throws SBOLValidationException {
		return document.createCopy(topLevel,displayId,version);
	}

	/**
	 * Creates a copy of the given TopLevel instance with the given arguments, and then adds it to
	 * the corresponding top-level list owned by this SBOLDocument object.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} is {@code null}, then it is extracted from the given
	 * {@code topLevel} instance. If it does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * If either the given {@code displayId} or {@code version}, then the corresponding field
	 * is extracted from the given {@code topLevel} instance. Both extracted fields are required
	 * to be valid and not {@code null}.
	 * <p>
	 * A top-level instance with a compliant URI is created using the given arguments,
	 * and then its display ID, persistent identity, and version fields are set. This
	 * instance is then added to the corresponding top-level list owned by this SBOLDocument object.
	 *
	 * @param topLevel
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @return the created top-level instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created top-level instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created top-level instance's identity URI
	 * already exists.
	 * @throws SBOLValidationException if the given {@code topLevel} instance is not an instance
	 * of a top-level object
	 */
	public static TopLevel createCopy(TopLevel topLevel, String URIprefix, String displayId, String version) throws SBOLValidationException {
		return document.createCopy(topLevel, URIprefix, displayId, version);
	}

	/**
	 * Removes the given {@code sequence} from this SBOLDocument object's list of Sequence instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param sequence
	 * @return {@code true} if the given {@code sequence} is successfully removed, {@code false} otherwise.
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and one of its ComponentDefinition instances refers to the given {@code sequence}
	 * (see {@link ComponentDefinition#containsSequence(URI)}).
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code sequence} is referenced by any of its Collection instances as a member.
	 */
	public static boolean removeSequence(Sequence sequence) throws SBOLValidationException {
		return document.removeSequence(sequence);
	}

	/**
	 * Returns the Sequence instance matching the given {@code displayId}
	 * and {@code version} from this SBOLDocument object's list of
	 * Sequence instances.
	 * <p>
	 * A compliant Sequence URI is created first using the {@code defaultURIprefix},
	 * the optional type {@link TopLevel#SEQUENCE}, the given {@code displayId}
	 * and {@code version}. This URI is used to look up the Sequence instance
	 * in this SBOLDocument object.
	 *
	 * @param displayId
	 * @param version
	 * @return the matching Sequence instance if present, or {@code null} otherwise.
	 */
	public static Sequence getSequence(String displayId,String version) {
		return document.getSequence(displayId, version);
	}

	/**
	 * Returns the Sequence instance matching the given {@code modelURI} from this
	 * SBOLDocument object's list of Sequence instances.
	 *
	 * @param sequenceURI
	 * @return the matching Sequence instance if present, or {@code null} otherwise.
	 */
	public static Sequence getSequence(URI sequenceURI) {
		return document.getSequence(sequenceURI);
	}

	/**
	 * Returns the set of {@code Sequence} instances owned by this SBOLDocument object.
	 *
	 * @return the set of {@code Sequence} instances owned by this SBOLDocument object.
	 */
	public static Set<Sequence> getSequences() {
		return document.getSequences();
	}

	/**
	 * Removes all entries in the list of Sequence instances
	 * owned by this SBOLDocument object. The list will be empty after this call returns.
	 * @throws SBOLValidationException 
	 */
	public static void clearSequences() throws SBOLValidationException {
		document.clearSequences();
	}

	/**
	 * Creates a GenericTopLevel instance with this SBOLDocument object's {@code defaultURIprefix},
	 * the given arguments, and an empty version string, and then
	 * adds it to this SBOLDocument object's list of GenericTopLevel instances.
	 * <p>
	 * This method calls {@link #createGenericTopLevel(String, String, String, QName)} to do the following
	 * validity checks and create a GenericTopLevel instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#",
	 * then "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} is not {@code null} and is valid.
	 * <p>
	 * A GenericTopLevel instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#GENERIC_TOP_LEVEL},
	 * the given {@code displayId}, and an empty version string.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param rdfType
	 * @return the created GenericTopLevel instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created GenericTopLevel instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created GenericTopLevel instance's identity URI
	 * exists in this SBOLDocument object's list of GenericTopLevel instances.
	 */
	public static GenericTopLevel createGenericTopLevel(String displayId, QName rdfType) throws SBOLValidationException {
		return document.createGenericTopLevel(displayId, rdfType);
	}

	/**
	 * Creates a GenericTopLevel instance with this SBOLDocument object's {@code defaultURIprefix}
	 * and the given arguments, and then adds it to this SBOLDocument object's list of GenericTopLevel instances.
	 * <p>
	 * This method calls {@link #createGenericTopLevel(String, String, String, QName)} to do the following
	 * validity checks and create a GenericTopLevel instance.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the {@code defaultURIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires the {@code defaultURIprefix} field to be set, and
	 * the given {@code displayId} and {@code version} arguments are not {@code null}
	 * and are both valid.
	 * <p>
	 * A GenericTopLevel instance is created with a compliant URI. This URI is composed from
	 * the this SBOLDocument object's {@code defaultURIprefix}, the optional type {@link TopLevel#GENERIC_TOP_LEVEL},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param displayId
	 * @param version
	 * @param rdfType
	 * @return the created GenericTopLevel instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created GenericTopLevel instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created GenericTopLevel instance's identity URI
	 * exists in this SBOLDocument object's list of GenericTopLevel instances.
	 */
	public static GenericTopLevel createGenericTopLevel(String displayId, String version, QName rdfType) throws SBOLValidationException {
		return document.createGenericTopLevel(displayId,version,rdfType);
	}

	/**
	 * Creates a GenericTopLevel instance with the given arguments, and then adds it to this SBOLDocument
	 * object's list of GenericTopLevel instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 * <p>
	 * If the given {@code URIprefix} does not end with one of the following delimiters: "/", ":", or "#", then
	 * "/" is appended to the end of it.
	 * <p>
	 * This method requires that the given {@code URIprefix}, {@code displayId},
	 * and {@code version} are not {@code null} and are valid.
	 * <p>
	 * A GenericTopLevel instance is created with a compliant URI. This URI is composed from
	 * the given {@code URIprefix}, the optional type {@link TopLevel#GENERIC_TOP_LEVEL},
	 * the given {@code displayId}, and {@code version}.
	 * The display ID, persistent identity, and version fields of this instance
	 * are then set accordingly.
	 *
	 * @param URIprefix
	 * @param displayId
	 * @param version
	 * @param rdfType
	 * @return the created GenericTopLevel instance
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if the {@code defaultURIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is {@code null}
	 * @throws SBOLValidationException if the given {@code URIprefix} is non-compliant
	 * @throws SBOLValidationException if the given {@code displayId} is invalid
	 * @throws SBOLValidationException if the given {@code version} is invalid
	 * @throws SBOLValidationException if the created GenericTopLevel instance's persistent
	 * identity exists in this SBOLDocument object's other lists of top-level instances.
	 * @throws SBOLValidationException if the created GenericTopLevel instance's identity URI
	 * exists in this SBOLDocument object's list of GenericTopLevel instances.
	 */
	public static GenericTopLevel createGenericTopLevel(String URIprefix, String displayId, String version, QName rdfType) throws SBOLValidationException {
		return document.createGenericTopLevel(URIprefix, displayId, version, rdfType);
	}

	/**
	 * Removes the given {@code genericTopLevel} from this SBOLDocument object's list of GenericTopLevel instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param genericTopLevel
	 * @return {@code true} if the given {@code genericTopLevel} is successfully removed, {@code false} otherwise.
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws SBOLValidationException if this SBOLDocument object is complete ({@link SBOLDocument#isComplete()}),
	 * and the given {@code genericTopLevel} is referenced by any of its Collection instances as a member.
	 */
	public static boolean removeGenericTopLevel(GenericTopLevel genericTopLevel) throws SBOLValidationException {
		return document.removeGenericTopLevel(genericTopLevel);
	}

	/**
	 * Returns the GenericTopLevel instance matching the given {@code displayId}
	 * and {@code version} from this SBOLDocument object's list of
	 * GenericTopLevel instances.
	 * <p>
	 * A compliant GenericTopLevel URI is created first using the {@code defaultURIprefix},
	 * the optional type {@link TopLevel#GENERIC_TOP_LEVEL}, the given {@code displayId}
	 * and {@code version}. This URI is used to look up the GenericTopLevel instance
	 * in this SBOLDocument object.
	 *
	 * @param displayId
	 * @param version
	 * @return the matching GenericTopLevel instance if present, or {@code null} otherwise.
	 */
	public static GenericTopLevel getGenericTopLevel(String displayId, String version) {
		return document.getGenericTopLevel(displayId, version);
	}

	/**
	 * Returns the GenericTopLevel instance matching the given {@code topLevelURI} from this
	 * SBOLDocument object's list of GenericTopLevel instances.
	 *
	 * @param topLevelURI
	 * @return the matching GenericTopLevel instance if present, or {@code null} otherwise.
	 */
	public static GenericTopLevel getGenericTopLevel(URI topLevelURI) {
		return document.getGenericTopLevel(topLevelURI);
	}

	/**
	 * Returns the set of {@code GenericTopLevel} instances owned by this SBOLDocument object.
	 *
	 * @return the set of {@code GenericTopLevel} instances owned by this SBOLDocument object.
	 */
	public static Set<GenericTopLevel> getGenericTopLevels() {
		return document.getGenericTopLevels();
	}

	/**
	 * Removes all entries in the list of GenericTopLevel instances
	 * owned by this SBOLDocument object. The list will be empty after this call returns.
	 * @throws SBOLValidationException 
	 */
	public static void clearGenericTopLevels() throws SBOLValidationException {
		document.clearGenericTopLevels();
	}

	/**
	 * Returns the top-level instance matching the given {@code topLevelURI} from this
	 * SBOLDocument object's lists of top-level instances.
	 *
	 * @param topLevelURI
	 * @return the matching top-level instance if present, or {@code null} otherwise.
	 */
	public static TopLevel getTopLevel(URI topLevelURI) {
		return document.getTopLevel(topLevelURI);
	}
	
	/**
	 * Returns a set of all TopLevel objects.
	 *
	 * @return set of all TopLevel objects.
	 */	
	public static Set<TopLevel> getTopLevels() {
		return document.getTopLevels();
	}

	/**
	 * Creates a set of TopLevels with derived from the same object
	 * as specified by the wasDerivedFrom parameter.
	 * @param wasDerivedFrom
	 * @return Set of TopLevels with a matching wasDerivedFrom URI.
	 */
	public static Set<TopLevel> getByWasDerivedFrom(URI wasDerivedFrom) {
		return document.getByWasDerivedFrom(wasDerivedFrom);
	}

	/**
	 * Adds a namespace URI and its prefix to a SBOL document
	 *
	 * @param nameSpaceURI The Namespace {@link URI}
	 * @param prefix The prefix {@link String}
	 */
	public static void addNamespace(URI nameSpaceURI, String prefix) {
		document.addNamespace(nameSpaceURI, prefix);
	}

	/**
	 * Adds a namespace {@link QName} to a SBOL document
	 *
	 * @param qName Qualified name ({@link QName}) for a namespace
	 */
	public static void addNamespace(QName qName) {
		document.addNamespace(qName);
	}

	/**
	 *  Removes all non-required namespaces from the SBOL document.
	 * @throws SBOLValidationException 
	 */
	public static void clearNamespaces() throws SBOLValidationException {
		document.clearNamespaces();
	}

	/**
	 * Returns the {@link QName} instance matching the given {@code modelURI} from this
	 * SBOLDocument object's list of namespace QName instances.
	 *
	 * @param namespaceURI
	 * @return the matching instance if present, or {@code null} otherwise.
	 */
	public static QName getNamespace(URI namespaceURI) {
		return document.getNamespace(namespaceURI);
	}

	/**
	 * Returns the list of namespace bindings owned by this SBOLDocument object.
	 *
	 * @return the list of namespace bindings owned by this SBOLDocument object.
	 */
	public static List<QName> getNamespaces() {
		return document.getNamespaces();
	}

	/**
	 * Removes the given {@code namespaceURI} from this SBOLDocument object's list of ModuleDefinition instances.
	 * <p>
	 * This SBOLDcouement object is checked for compliance first. Only a compliant SBOLDocument instance
	 * is allowed to be edited.
	 *
	 * @param namespaceURI
	 * @throws SBOLValidationException if this SBOLDocument object is not compliant
	 * @throws IllegalStateException if the given {@code namespaceURI} belongs to one of the
	 * following required namespace binding: {@link Sbol2Terms#sbol2}, {@link Sbol2Terms#dc},
	 * {@link Sbol2Terms#prov}, or {@link Sbol1Terms#rdf}.
	 */
	public static void removeNamespace(URI namespaceURI) throws SBOLValidationException {
		document.removeNamespace(namespaceURI);
	}

	@Override
	public int hashCode() {
		return document.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		return document.equals(obj);
	}

	/**
	 * Sets the default URI prefix to the given {@code defaultURIprefix}.
	 *
	 * @param defaultURIprefix
	 * @throws SBOLValidationException 
	 */

	public static void setDefaultURIprefix(String defaultURIprefix) throws SBOLValidationException {
		document.setDefaultURIprefix(defaultURIprefix);
	}

	/**
	 * Returns the default URI prefix of this SBOLDocument object
	 *
	 * @return the default URI prefix of this SBOLDocument object
	 */
	public static String getDefaultURIprefix() {
		return document.getDefaultURIprefix();
	}

	/**
	 * Returns {@code true} if the {@code complete} flag for this SBOLDocument object is set.
	 * This flag is set to {@code true} if all objects are must be present within this SBOLDocument object,
	 * i.e. all URI references point to actual objects.
	 *
	 * @return {@code true} if the complete flag is set, {@code false} otherwise
	 */
	public static boolean isComplete() {
		return document.isComplete();
	}

	/**
	 * Sets the complete flag which when true indicates this SBOLDocument object is complete
	 * and any URIs that cannot be dereferenced to a valid object cause an exception to be thrown.
	 *
	 * @param complete
	 */
	public static void setComplete(boolean complete) {
		document.setComplete(complete);
	}

	/**
	 * Returns {@code true} if all URIs in this SBOLDocument object are compliant.
	 *
	 * @return {@code true} if all URIs in this SBOLDocument object are compliant,
	 * {@code false} otherwise
	 */
	public static boolean isCompliant() {
		return document.isCompliant();
	}

	/**
	 * Returns {@code true} if types are to be inserted into top-level URIs.
	 *
	 * @return {@code true} if types are to be inserted into top-level URIs, {@code false} otherwise
	 */
	public static boolean isTypesInURIs() {
		return document.isTypesInURIs();
	}

	/**
	 * Sets the flag to the given {@code typesInURIs} to determine if types are to be inserted into top-level URIs.
	 *
	 * @param typesInURIs
	 */
	public static void setTypesInURIs(boolean typesInURIs) {
		document.setTypesInURIs(typesInURIs);
	}

	/**
	 * Returns {@code true} if default component instances should be created when not present.
	 *
	 * @return {@code true} if default component instances should be created when not present,
	 * {@code false} otherwise
	 */
	public static boolean isCreateDefaults() {
		return document.isCreateDefaults();
	}

	/**
	 * Sets the flag to the given {@code createDefaults} to determine if default component instances
	 * should be created when not present.
	 *
	 * @param createDefaults
	 */
	public static void setCreateDefaults(boolean createDefaults) {
		document.setCreateDefaults(createDefaults);
	}

	/**
	 * Takes in a given RDF fileName and add the data read to this SBOLDocument.
	 *
	 * @param fileName
	 * @throws CoreIoException
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * @throws SBOLValidationException 
	 */
	public static void read(String fileName) throws CoreIoException, XMLStreamException, FactoryConfigurationError, FileNotFoundException, SBOLValidationException {
		document.read(fileName);
	}

	/**
	 * Takes in a given RDF File and add the data read to this SBOLDocument.
	 *
	 * @param file
	 * @throws CoreIoException
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * @throws SBOLValidationException 
	 */
	public static void read(File file) throws CoreIoException, XMLStreamException, FactoryConfigurationError, FileNotFoundException, SBOLValidationException {
		document.read(file);
	}

	/**
	 * Takes in a given RDF InputStream and add the data read to this SBOLDocument.
	 *
	 * @param in
	 * @throws CoreIoException
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 * @throws SBOLValidationException 
	 */
	public static void read(InputStream in) throws CoreIoException, XMLStreamException, FactoryConfigurationError, SBOLValidationException {
		document.read(in);
	}

	/**
	 * Serializes SBOLDocument and outputs the data from the serialization to the given output
	 * file name in RDF format
	 * @param filename
	 * @throws IOException
	 * @throws CoreIoException
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 */
	public static void write(String filename) throws XMLStreamException, FactoryConfigurationError, CoreIoException, IOException
	{
		document.write(filename);
	}

	/**
	 * Serializes SBOLDocument and outputs the data from the serialization to the given output
	 * file in RDF format
	 * @param file
	 * @throws CoreIoException
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static void write(File file) throws XMLStreamException, FactoryConfigurationError, CoreIoException, IOException
	{
		document.write(file);
	}

	/**
	 * Serializes SBOLDocument and outputs the data from the serialization to the given output
	 * stream in RDF format
	 * @param out
	 * @throws CoreIoException
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static void write(OutputStream out) throws XMLStreamException, FactoryConfigurationError, CoreIoException, IOException
	{
		document.write(out);
	}

	@Override
	public String toString() {
		return document.toString();
	}
}