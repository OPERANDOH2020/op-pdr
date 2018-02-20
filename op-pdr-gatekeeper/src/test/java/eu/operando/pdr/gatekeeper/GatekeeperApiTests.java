package eu.operando.pdr.gatekeeper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import eu.operando.OperandoCommunicationException;
import eu.operando.UnableToGetDataException;
import eu.operando.api.AuthenticationService;

@RunWith(Parameterized.class)
public class GatekeeperApiTests
{
	private static final String SERVICE_ID_GATEKEEPER = "/gatekeeper";
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";

	@Rule public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private AuthenticationService authenticationDelegate;
	
	@Mock
	private GatekeeperService gkDelegate;
	
	@InjectMocks
	private GatekeeperApi api = new GatekeeperApi();
	
	@Parameter
	public ApiMethod httpMethod;
	
	@Parameters(name="{0}")
	public static Collection<Object[]> data()
	{
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		for (ApiMethod method : ApiMethod.class.getEnumConstants())
		{
			data.add(new Object[] { method });
		}		
		
		return data;
	}
		
	@Test
	public void testProcessHttpMethodRequest_AuthenticationDelegateInvoked() throws OperandoCommunicationException, UnableToGetDataException
	{
		// Set up -- test case
		String serviceTicket = "st";
		when(authenticationDelegate.isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER)).thenReturn(false);
		
		// Set up -- common
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		when(headers.getHeaderString(HEADER_NAME_SERVICE_TICKET)).thenReturn(serviceTicket);
		String pathPlus = "/plus";
		UriInfo uriInfo = Mockito.mock(UriInfo.class);		

		// Exercise
		exerciseApiMethod(headers, pathPlus, httpMethod.getBody(), uriInfo);

		// Verify
		verify(authenticationDelegate).isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER);
	}

	@Test(expected = UnableToGetDataException.class)
	public void testProcessHttpMethodRequest_CannotAuthenticate_ThrowException() throws OperandoCommunicationException, UnableToGetDataException
	{
		// Set up -- test case
		String serviceTicket = "st";
		when(authenticationDelegate.isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER)).thenThrow(UnableToGetDataException.class);
		
		// Set up -- common
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		when(headers.getHeaderString(HEADER_NAME_SERVICE_TICKET)).thenReturn(serviceTicket);
		String pathPlus = "/plus";
		UriInfo uriInfo = Mockito.mock(UriInfo.class);

		// Exercise
		exerciseApiMethod(headers, pathPlus, httpMethod.getBody(), uriInfo);
		
		// Verify - expect exception in attribute
	}

	@Test
	public void testProcessHttpMethodRequest_NotAuthenticated_DelegateNotCalled() throws OperandoCommunicationException, UnableToGetDataException
	{
		// Set up -- test case
		String serviceTicket = "badSt";
		when(authenticationDelegate.isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER)).thenReturn(false);
		
		// Set up -- common
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		when(headers.getHeaderString(HEADER_NAME_SERVICE_TICKET)).thenReturn(serviceTicket);
		String pathPlus = "/plus";
		UriInfo uriInfo = Mockito.mock(UriInfo.class);

		// Exercise
		exerciseApiMethod(headers, pathPlus, httpMethod.getBody(), uriInfo);

		// Verify
		verifyZeroInteractions(gkDelegate);
	}

	@Test
	public void testProcessHttpMethodRequest_NotAuthenticated_ReturnUnauthorisedCode() throws OperandoCommunicationException, UnableToGetDataException
	{
		// Set up -- test case
		String serviceTicket = "badSt";
		when(authenticationDelegate.isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER)).thenReturn(false);
		
		// Set up -- common
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		when(headers.getHeaderString(HEADER_NAME_SERVICE_TICKET)).thenReturn(serviceTicket);
		String pathPlus = "/plus";
		UriInfo uriInfo = Mockito.mock(UriInfo.class);

		// Exercise
		Response response = exerciseApiMethod(headers, pathPlus, httpMethod.getBody(), uriInfo);

		// Verify
		int statusCodeResponse = response.getStatus();
		assertEquals("If the OSP is not authenticated, the API should return an unauthorized code.", Status.UNAUTHORIZED.getStatusCode(), statusCodeResponse);
	}

	@Test
	public void testProcessHttpMethodRequest_Authenticated_ResponseFromDelegateReturned() throws OperandoCommunicationException, UnableToGetDataException
	{
		// Set up
		String serviceTicket = "st";
		when(authenticationDelegate.isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER)).thenReturn(true);
		
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		when(headers.getHeaderString(HEADER_NAME_SERVICE_TICKET)).thenReturn(serviceTicket);
		
		String pathPlus = "/plus";
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		
		Response expectedResponse = Response.ok().entity("entity").build();
		when(gkDelegate.processRequest(pathPlus, httpMethod.toString(), headers, uriInfo.getQueryParameters(), httpMethod.getBody())).thenReturn(expectedResponse);

		// Exercise
		Response response = exerciseApiMethod(headers, pathPlus, httpMethod.getBody(), uriInfo);

		// Verify
		assertEquals(expectedResponse, response);
	}
	
	public Response exerciseApiMethod(HttpHeaders headers, String pathPlus, String body, UriInfo uriInfo) throws UnableToGetDataException
	{
		switch (httpMethod)
		{
			case DELETE:
				return api.processDeleteRequest(headers, pathPlus, uriInfo);
			case GET:
				return api.processGetRequest(headers, pathPlus, uriInfo);
			case POST:
				return api.processPostRequest(headers, pathPlus, body, uriInfo);
			case PUT:
				return api.processPutRequest(headers, pathPlus, body, uriInfo);
			default:
				throw new IllegalArgumentException();
		}
	}

	public enum ApiMethod
	{
		GET(HttpMethod.GET, ""),
		POST(HttpMethod.POST, "body"),
		PUT(HttpMethod.PUT, "body"),
		DELETE(HttpMethod.DELETE, "");
		
		private String name;
		private String body;
		
		ApiMethod(String name, String body)
		{
			this.name = name;
			this.body = body;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
		
		public String getBody()
		{
			return body;
		}
	}
}
