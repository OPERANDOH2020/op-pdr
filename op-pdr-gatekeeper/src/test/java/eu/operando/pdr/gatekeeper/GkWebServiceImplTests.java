package eu.operando.pdr.gatekeeper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoAuthenticationException;
import eu.operando.OperandoCommunicationException;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;
import eu.operando.moduleclients.ClientDataAccessNode;

@RunWith(Parameterized.class)
public class GkWebServiceImplTests
{
	private static final String HEADER_NAME_OSP_ID = "osp-identifier";
	// Variables to test
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";
	private static final String HEADER_NAME_HOST = "host";
	private static final String SERVICE_ID_GATEKEEPER = "/gatekeeper";

	// Variables to assist testing
	private static final String PATH_PLUS = "path_plus";
	private static final String SERVICE_TICKET_CALLER = "ST-1234";
	private static final String PSP_USER_ID_CALLER = "user";
	private ClientAuthenticationApiOperandoService mockClientAuthenticationService = Mockito.mock(ClientAuthenticationApiOperandoService.class);
	private ClientDataAccessNode mockClientDataAccessNode = Mockito.mock(ClientDataAccessNode.class);
	private HttpHeaders mockHeaders = Mockito.mock(HttpHeaders.class);
	private MultivaluedMap<String, String> stubbedHeadersFromCaller = new MultivaluedStringMap();

	// System under test
	private GkWebServiceImpl service = new GkWebServiceImpl(mockClientAuthenticationService, mockClientDataAccessNode);

	// Parameters to tests
	@Parameter(value = 0)
	public String httpMethod;
	@Parameter(value = 1)
	public int numOverloadedMethodParameters;
	@Parameter(value = 2)
	public String body;

	/**
	 * The service should behave similarly for each http method and for each of the overloaded methods.
	 */
	@Parameters(name = "process {0} request with {1} arguments (body = \"{2}\")")
	public static Collection<Object[]> data()
	{
		// Work out parameters
		String[] httpMethods = new String[] { HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE };
		Integer[] numsOverloadedMethodParameters = new Integer[] { 3, 4 };

		// Put it all together!
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		for (String httpMethod : httpMethods)
		{
			for (int numOverloadedMethodParameters : numsOverloadedMethodParameters)
			{
				String body = "";
				if (numOverloadedMethodParameters == 4)
				{
					body = "body";
				}
				data.add(new Object[] { httpMethod, numOverloadedMethodParameters, body });
			}
		}

		return data;
	}

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
	}

	@Test
	public void testProcessRequest_AuthenticationDetailsRequestedFromAuthenticationServiceCorrectServiceTicketUsed() throws OperandoCommunicationException
	{
		// Set up
		when(mockClientAuthenticationService.requestAuthenticationDetails(SERVICE_TICKET_CALLER, SERVICE_ID_GATEKEEPER))
			.thenReturn(new AuthenticationWrapper(false, PSP_USER_ID_CALLER));

		exerciseProcessRequest();

		// Verify
		Mockito.verify(mockClientAuthenticationService)
			.requestAuthenticationDetails(SERVICE_TICKET_CALLER, SERVICE_ID_GATEKEEPER);
	}

	@Test
	public void testProcessRequest_TicketInvalid_RequestNotForwardedToDan() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Set up
		when(mockClientAuthenticationService.requestAuthenticationDetails(SERVICE_TICKET_CALLER, SERVICE_ID_GATEKEEPER))
			.thenReturn(new AuthenticationWrapper(false, PSP_USER_ID_CALLER));

		exerciseProcessRequest();

		// Verify
		Mockito.verify(mockClientDataAccessNode, never())
			.sendRequest(anyString(), anyString(), anyString(), any(MultivaluedMap.class), anyString());
	}

	@Test
	public void testProcessRequest_TicketInvalid_UnauthorizedResponseReturned() throws OperandoCommunicationException
	{
		// Set up
		when(mockClientAuthenticationService.requestAuthenticationDetails(SERVICE_TICKET_CALLER, SERVICE_ID_GATEKEEPER))
			.thenReturn(new AuthenticationWrapper(false, PSP_USER_ID_CALLER));

		// Exercise
		Response response = exerciseProcessRequest();

		// Verify
		assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatus());
		assertEquals("Invalid service ticket", response.getEntity());
	}

	@Test
	public void testProcessRequest_TicketValid_RequestForwardedToDanCorrectly() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Set up
		when(mockClientAuthenticationService.requestAuthenticationDetails(SERVICE_TICKET_CALLER, SERVICE_ID_GATEKEEPER))
			.thenReturn(new AuthenticationWrapper(true, PSP_USER_ID_CALLER));

		// Exercise
		exerciseProcessRequest();

		// Verify
		MultivaluedMap<String, String> headersExpectedToDan = new MultivaluedStringMap();
		headersExpectedToDan.putAll(stubbedHeadersFromCaller);
		headersExpectedToDan.remove(HEADER_NAME_HOST);
		headersExpectedToDan.remove(HEADER_NAME_SERVICE_TICKET);

		Mockito.verify(mockClientDataAccessNode)
			.sendRequest(PSP_USER_ID_CALLER, PATH_PLUS, httpMethod, headersExpectedToDan, body);
	}

	@Test
	public void testProcessRequest_TicketValid_ResponseFromDanReturned() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Set up
		when(mockClientAuthenticationService.requestAuthenticationDetails(SERVICE_TICKET_CALLER, SERVICE_ID_GATEKEEPER))
			.thenReturn(new AuthenticationWrapper(true, PSP_USER_ID_CALLER));
		Response responseFromDan = Response.ok().build();
		when(mockClientDataAccessNode.sendRequest(eq(PSP_USER_ID_CALLER), eq(PATH_PLUS), eq(httpMethod), any(MultivaluedMap.class), eq(body)))
			.thenReturn(responseFromDan);

		// Exercise
		Response responseActual = exerciseProcessRequest();

		// Verify
		assertEquals(responseFromDan, responseActual);
	}

	private Response exerciseProcessRequest()
	{
		Response response = null;

		if (numOverloadedMethodParameters == 3)
		{
			response = service.processRequest(PATH_PLUS, httpMethod, mockHeaders);
		}
		else if (numOverloadedMethodParameters == 4)
		{
			response = service.processRequest(PATH_PLUS, httpMethod, mockHeaders, body);
		}

		return response;
	}
}
