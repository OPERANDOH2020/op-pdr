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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import eu.operando.AuthenticationWrapper;
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
		return processRequest(HttpMethod.GET, headers, pathPlus, uriInfo, "");
	}

	@POST
	@Path("/{pathPlus : .*}")
	public Response processPostRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, String body, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		return processRequest(HttpMethod.POST, headers, pathPlus, uriInfo, body);
	}

	@PUT
	@Path("/{pathPlus : .*}")
	public Response processPutRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, String body, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		return processRequest(HttpMethod.PUT, headers, pathPlus, uriInfo, body);
	}

	@DELETE
	@Path("/{pathPlus : .*}")
	public Response processDeleteRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, @Context UriInfo uriInfo) throws UnableToGetDataException
	{
		return processRequest(HttpMethod.DELETE, headers, pathPlus, uriInfo, "");
	}

	private Response processRequest(String httpMethod, HttpHeaders headers, String pathPlus, UriInfo uriInfo, String body) throws UnableToGetDataException
	{
		String serviceTicket = headers.getHeaderString(HEADER_NAME_SERVICE_TICKET);
		if (serviceTicket == null)
		{
			return noTicketResponseBuilder.build();
		}
		
		AuthenticationWrapper wrapper = authenticationDelegate.requestAuthenticationDetails(serviceTicket, SERVICE_ID_GATEKEEPER);		
		if (!wrapper.isTicketValid())
		{
			return invalidTicketResponseBuilder.build();
		}
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, httpMethod, headers, queryParameters, body, wrapper.getIdOspUser());
	}
}
