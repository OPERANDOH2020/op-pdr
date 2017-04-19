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

/**
 * Due to changes in the work-flow for handling requests to access data, the Gatekeeper is now essentially a placeholder module (which will manage
 * additional up-front validation of requests beyond checking authentication), which currently functions as a proxy to the Data Access Node.
 * 
 */
@Path("resources")
public class GkWebInterface
{
	private GkWebService service = GkWebServiceFactory.create(); 
	
	@GET
	@Path("/{pathPlus : .*}")
	public Response processGetRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, @Context UriInfo uriInfo)
	{
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.GET, headers, queryParameters);
	}

	@POST
	@Path("/{pathPlus : .*}")
	public Response processPostRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, String body, @Context UriInfo uriInfo)
	{
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.POST, headers, queryParameters, body);
	}

	@PUT
	@Path("/{pathPlus : .*}")
	public Response processPutRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, String body, @Context UriInfo uriInfo)
	{
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.PUT, headers, queryParameters, body);
	}

	@DELETE
	@Path("/{pathPlus : .*}")
	public Response processDeleteRequest(@Context HttpHeaders headers, @PathParam("pathPlus") String pathPlus, @Context UriInfo uriInfo)
	{
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		return service.processRequest(pathPlus, HttpMethod.DELETE, headers, queryParameters);
	}
}
