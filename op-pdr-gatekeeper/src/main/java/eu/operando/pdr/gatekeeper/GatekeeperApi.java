package eu.operando.pdr.gatekeeper;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import eu.operando.UnableToGetDataException;
import eu.operando.api.AuthenticationService;
import eu.operando.api.factories.AuthenticationServiceFactory;

/**
 * Due to changes in the work-flow for handling requests to access data, the Gatekeeper is now essentially a placeholder module (which will manage
 * additional up-front validation of requests beyond checking authentication), which currently functions as a proxy to the Data Access Node.
 * 
 */
@Path("resources")
public class GatekeeperApi
{
	public static final String SERVICE_ID_GATEKEEPER = "/gatekeeper";
	public static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";
	
	private static final ResponseBuilder noTicketResponseBuilder = Response.status(Status.BAD_REQUEST).entity("A service ticket must be provided using the service-ticket header.");
	private static final ResponseBuilder invalidTicketResponseBuilder = Response.status(Status.UNAUTHORIZED).entity("Invalid service ticket");
	
	
	private AuthenticationService authenticationDelegate;
	private GatekeeperService service;
	
	public GatekeeperApi()
	{		
		authenticationDelegate = AuthenticationServiceFactory.getAuthenticationService(Config.PROPERTIES_FILE_GK);
		service = GatekeeperServiceFactory.create();
	}
	
	@GET
	@Path("/{pathPlus : .*}")
	public Response processGetRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		Response errorResponse = validateRequest(headers);
		if (errorResponse != null)
		{
			return errorResponse;
		}
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.GET, headers, queryParameters, "");
	}

	@POST
	@Path("/{pathPlus : .*}")
	public Response processPostRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, String body, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		Response errorResponse = validateRequest(headers);
		if (errorResponse != null)
		{
			return errorResponse;
		}
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.POST, headers, queryParameters, body);
	}

	@PUT
	@Path("/{pathPlus : .*}")
	public Response processPutRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, String body, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		Response errorResponse = validateRequest(headers);
		if (errorResponse != null)
		{
			return errorResponse;
		}
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.PUT, headers, queryParameters, body);
	}

	@DELETE
	@Path("/{pathPlus : .*}")
	public Response processDeleteRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		Response errorResponse = validateRequest(headers);
		if (errorResponse != null)
		{
			return errorResponse;
		}
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.DELETE, headers, queryParameters, "");
	}

	/**
	 * Validates an incoming HTTP request.
	 * @param headers the headers in the request.
	 * @return an appropriate response if there is a validation error, null otherwise.
	 * @throws UnableToGetDataException if there is an error in inter-module communication
	 */
	private Response validateRequest(HttpHeaders headers) throws UnableToGetDataException
	{
		String serviceTicket = headers.getHeaderString(HEADER_NAME_SERVICE_TICKET);
		
		if (serviceTicket == null)
		{
			return noTicketResponseBuilder.build();
		}
		
		if (!authenticationDelegate.isAuthenticatedForService(serviceTicket, SERVICE_ID_GATEKEEPER))
		{
			return invalidTicketResponseBuilder.build();
		}
		
		return null;
	}
}
