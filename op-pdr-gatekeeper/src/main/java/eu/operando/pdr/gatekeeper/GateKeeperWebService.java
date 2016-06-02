package eu.operando.pdr.gatekeeper;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpStatus;

/**
 * The GateKeeper (GK) supports controlled release of personal user data,
 * ensuring that OSPs requests are validated before releasing the data to them.
 * 
 * <p> The GK module is part of the PDR container. It receives, authenticates, and validates
 * requests from OSPs for user data. Upon receipt of a request for user data the GK
 * attempts to authenticate the requesting entity with the Authentication Service (AS).
 * Then the GK requests authorisation to access the data from the Rights Management (RM) module,
 * which validates the request and provides specific access permissions to individual fields.
 * 
 * <p> If the validation or authorisation fails, the OSP request is denied.
 * Otherwise, a security token (STK) is generated and a response is sent to the requesting entity
 * with STK and an URL of a Data Access Node.
 */
//@Path(ClientOperandoModule.PATH_EXTERNAL_OPERANDO_GATEKEEPER)
@Path("/data_request")
public class GateKeeperWebService
{
	private GateKeeperClientI client = null;
	
	/**
	 * This constructor is needed for the web server (e.g. tomcat) to make use of this class.
	 */
	public GateKeeperWebService()
	{
		//TODO - for testing. Remove when can unit test properly.
		//client = new GateKeeperClientStub(false, false, "token", "url");
		//client = new GateKeeperClientStub(true, false, "token", "url");
		//client = new GateKeeperClientStub(true, true, "token", "url");
		
		//TODO - make these hosts configurable.
		client = new GateKeeperClient("http://localhost:8080", "http://localhost:8080", "http://localhost:8080", "http://localhost:8080");
	}
	
	public GateKeeperWebService(GateKeeperClientI client)
	{
		this.client = client;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleOspDataAccessRequest(GateKeeperRequestWrapper wrapper)
	{
		//Read the relevant details from the DTO.
		String serviceTicket = wrapper.getServiceTicket();
		int ospId = wrapper.getOspId();
		int queryId = wrapper.getQueryId();
		Vector<Integer> userIds = wrapper.getUserIds();
		
		//Variables for the response that could be passed back to the OSP.
		int statusCode = HttpStatus.SC_OK;
		GateKeeperResponse responseDto = new GateKeeperResponse();
				
		//Check the OSP's credentials with the AS module.
		boolean isOspAuthenticated = client.isOspAuthenticated(serviceTicket);
		
		if (isOspAuthenticated)
		{
			//If the authentication was successful, check that the OSP is authorised to access the requested data.
			AuthorisationWrapper authorisationWrapper = client.authoriseOsp(ospId, queryId, userIds);
			boolean isQueryPermissible = authorisationWrapper.isQueryPermissible();
			
			if (isQueryPermissible)
			{
				//If the OSP is authorised, set the values to be returned in the message body.
				String danUrl = client.getDanUrlForQuery(ospId, queryId, userIds);
				responseDto.setDanUrl(danUrl);
				
				String securityToken = authorisationWrapper.getSecurityToken();
				responseDto.setSecurityToken(securityToken);
			}
			else
			{
				//If the OSP is unauthorised, return a forbidden code.
				statusCode = HttpStatus.SC_FORBIDDEN;
			}
		}
		else
		{
			//If the authentication was unsuccessful, return an unauthorised code.
			statusCode = HttpStatus.SC_UNAUTHORIZED;
		}
		
		//Build up the response.
		ResponseBuilder responseBuilder = Response.status(statusCode);
		if (statusCode == HttpStatus.SC_OK)
		{
			responseBuilder.entity(responseDto);
		}
		
		//Return the response.
		return responseBuilder.build();
	}
}
