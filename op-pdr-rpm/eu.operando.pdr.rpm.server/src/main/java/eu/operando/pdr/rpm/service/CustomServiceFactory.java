package eu.operando.pdr.rpm.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataErrorCallback;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPAErrorCallback;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAAccessFactory;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAFactory;

public class CustomServiceFactory extends ODataServiceFactory {
	private static final String PERSISTENCE_UNIT_NAME = "PU";

	private ODataJPAContext oDataJPAContext;
	private ODataContext oDataContext;
	private boolean setDetailErrors = false;

	/**
	 * Creates an OData Service based on the values set in
	 * {@link org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext} and
	 * {@link org.apache.olingo.odata2.api.processor.ODataContext}.
	 */
	@Override
	public final ODataService createService(final ODataContext ctx) throws ODataException {

		oDataContext = ctx;

		// Initialize OData JPA Context
		oDataJPAContext = initializeODataJPAContext();

		validatePreConditions();

		ODataJPAFactory factory = ODataJPAFactory.createFactory();
		ODataJPAAccessFactory accessFactory = factory.getODataJPAAccessFactory();

		// OData JPA Processor
		if (oDataJPAContext.getODataContext() == null) {
			oDataJPAContext.setODataContext(ctx);
		}

		ODataSingleProcessor odataJPAProcessor = new CustomODataJPAProcessor(oDataJPAContext);

		// OData Entity Data Model Provider based on JPA
		EdmProvider edmProvider = accessFactory.createJPAEdmProvider(oDataJPAContext);

		return createODataSingleProcessorService(edmProvider, odataJPAProcessor);
	}

	private void validatePreConditions() throws ODataJPARuntimeException {

		if (oDataJPAContext.getEntityManagerFactory() == null) {
			throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.ENTITY_MANAGER_NOT_INITIALIZED, null);
		}

	}

	/**
	 * Implement this method and initialize OData JPA Context. It is mandatory
	 * to set an instance of type {@link javax.persistence.EntityManagerFactory} into the context. An exception of type
	 * {@link org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException} is thrown if
	 * EntityManagerFactory is not initialized. <br>
	 * <br>
	 * <b>Sample Code:</b> <code>
	 * <p>public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{</p>
	 * 
	 * <blockquote>private static final String PUNIT_NAME = "punit";
	 * <br>
	 * public ODataJPAContext initializeODataJPAContext() {
	 * <blockquote>ODataJPAContext oDataJPAContext = this.getODataJPAContext();
	 * <br>
	 * EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
	 * <br>
	 * oDataJPAContext.setEntityManagerFactory(emf);
	 * oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);
	 * <br> return oDataJPAContext;</blockquote>
	 * }</blockquote>
	 * } </code>
	 * <p>
	 * 
	 * @return an instance of type {@link org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext}
	 * @throws ODataJPARuntimeException
	 */
	public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException{
		ODataJPAContext oDataJPAContext = this.getODataJPAContext();
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			oDataJPAContext.setEntityManagerFactory(emf);
			oDataJPAContext.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
			return oDataJPAContext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return an instance of type {@link ODataJPAContext}
	 * @throws ODataJPARuntimeException
	 */
	public final ODataJPAContext getODataJPAContext() throws ODataJPARuntimeException {
		if (oDataJPAContext == null) {
			oDataJPAContext = ODataJPAFactory.createFactory().getODataJPAAccessFactory().createODataJPAContext();
		}
		if (oDataContext != null) {
			oDataJPAContext.setODataContext(oDataContext);
		}
		return oDataJPAContext;

	}

	/**
	 * The method sets the context whether a detail error message should be thrown
	 * or a less detail error message should be thrown by the library.
	 * @param setDetailErrors takes
	 * <ul><li>true - to indicate that library should throw a detailed error message</li>
	 * <li>false - to indicate that library should not throw a detailed error message</li>
	 * </ul>
	 * 
	 */
	protected void setDetailErrors(final boolean setDetailErrors) {
		this.setDetailErrors = setDetailErrors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
		if (setDetailErrors == true) {
			if (callbackInterface.isAssignableFrom(ODataErrorCallback.class)) {
				return (T) new ODataJPAErrorCallback();
			}
		}
		return null;
	}

}
