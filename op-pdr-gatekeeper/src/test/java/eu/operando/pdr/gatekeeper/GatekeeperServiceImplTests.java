package eu.operando.pdr.gatekeeper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import eu.operando.OperandoAuthenticationException;
import eu.operando.OperandoCommunicationException;
import eu.operando.moduleclients.ClientRightsManagement;

public class GatekeeperServiceImplTests
{
	private static final String HEADER_NAME_OSP_ID = "osp-identifier";
	// Variables to test
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";
	private static final String HEADER_NAME_HOST = "host";

	// Variables to assist testing
	private static final String PATH_PLUS = "path_plus";
	private static final String SERVICE_TICKET_CALLER = "ST-1234";
	private static final String PSP_USER_ID_CALLER = "user";
	private static final String BODY = "body";
	private static final String HTTP_METHOD = "method";;
	private ClientRightsManagement mockClientRightsManagement = Mockito.mock(ClientRightsManagement.class);
	private HttpHeaders mockHeaders = Mockito.mock(HttpHeaders.class);
	private MultivaluedMap<String, String> stubbedHeadersFromCaller = new MultivaluedStringMap();

	// System under test
	private GatekeeperServiceImpl service = new GatekeeperServiceImpl(mockClientRightsManagement);
	
	private MultivaluedMap<String, String> queryParameters;
	
	@Before
	public void setUp()
	{
		// Some
		stubbedHeadersFromCaller.add("key1", "value1");
		stubbedHeadersFromCaller.add("key2", "value2");
		stubbedHeadersFromCaller.add(HEADER_NAME_HOST, "www.operando.eu");
		stubbedHeadersFromCaller.add(HEADER_NAME_OSP_ID, "OSP1");
		stubbedHeadersFromCaller.add(HEADER_NAME_SERVICE_TICKET, SERVICE_TICKET_CALLER);
		
		when(mockHeaders.getRequestHeaders()).thenReturn(stubbedHeadersFromCaller);
		when(mockHeaders.getHeaderString(HEADER_NAME_SERVICE_TICKET)).thenReturn(SERVICE_TICKET_CALLER);
		
		queryParameters = new MultivaluedStringMap();
		queryParameters.add("key", "value");
	}

	@Test
	public void testProcessRequest_RequestForwardedToRmCorrectly() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Exercise
		service.processRequest(PATH_PLUS, HTTP_METHOD, mockHeaders, queryParameters, BODY, PSP_USER_ID_CALLER);

		// Verify
		MultivaluedMap<String, String> headersExpectedToRm = new MultivaluedStringMap();
		headersExpectedToRm.putAll(stubbedHeadersFromCaller);
		headersExpectedToRm.remove(HEADER_NAME_HOST);
		headersExpectedToRm.remove(HEADER_NAME_SERVICE_TICKET);

		Mockito.verify(mockClientRightsManagement).sendRequest(HTTP_METHOD, headersExpectedToRm, PSP_USER_ID_CALLER, PATH_PLUS, queryParameters, BODY);
	}

	@Test
	public void testProcessRequest_ResponseFromRmReturned() throws OperandoCommunicationException, OperandoAuthenticationException
	{		
		// Set up		
		Response responseFromRm = Response.ok().build();
		when(mockClientRightsManagement.sendRequest(eq(HTTP_METHOD), Matchers.<MultivaluedMap<String, String>>any(), eq(PSP_USER_ID_CALLER), eq(PATH_PLUS), eq(queryParameters),  eq(BODY)))
			.thenReturn(responseFromRm);

		// Exercise
		Response responseActual = service.processRequest(PATH_PLUS, HTTP_METHOD, mockHeaders, queryParameters, BODY, PSP_USER_ID_CALLER);

		// Verify
		assertEquals(responseFromRm, responseActual);
	}
}
