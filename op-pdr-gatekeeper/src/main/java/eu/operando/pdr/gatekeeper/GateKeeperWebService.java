package eu.operando.pdr.gatekeeper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.operando.Utils;
import eu.operando.moduleclients.ClientAuthenticationService;
import eu.operando.pdr.gatekeeper.message.DtoGateKeeperRequest;
import eu.operando.pdr.gatekeeper.message.DtoGateKeeperResponse;

/**
 * The GateKeeper (GK) supports controlled release of personal user data, ensuring that OSPs requests are validated before releasing the data to them.
 * 
 * <p>
 * The GK module is part of the PDR container. It receives, authenticates, and validates requests from OSPs for user data. Upon receipt of a request
 * for user data the GK attempts to authenticate the requesting entity with the Authentication Service (AS). Then the GK requests authorisation to
 * access the data from the Rights Management (RM) module, which validates the request and provides specific access permissions to individual fields.
 * 
 * <p>
 * If the validation or authorisation fails, the OSP request is denied. Otherwise, a security token (STK) is generated and a response is sent to the
 * requesting entity with STK and an URL of a Data Access Node.
 */
// @Path(ClientOperandoModule.PATH_EXTERNAL_OPERANDO_GATEKEEPER)
@Path("/data_request")
public class GateKeeperWebService
{
	// Log4j logging.
	private static final Logger LOGGER = LogManager.getLogger(GateKeeperWebService.class);

	// Location of properties file.
	private static final String PROPERTIES_FILE_GATEKEEPER = "config.properties";

	// Properties file property names.
	private static final String PROPERTY_NAME_ORIGIN_AUTHENTICATION_API = "originAuthenticationApi";
	//private static final String PROPERTY_NAME_ORIGIN_LOG_DB = "originLogDb";
	//private static final String PROPERTY_NAME_ORIGIN_DATA_ACCESS_NODE = "originDataAccessNode";

	// Properties file property values.
	private static final String ORIGIN_AUTHENTICATION_API = Utils.loadPropertyString(PROPERTIES_FILE_GATEKEEPER, PROPERTY_NAME_ORIGIN_AUTHENTICATION_API);
	//private static final String ORIGIN_LOG_DB = Utils.loadPropertyString(PROPERTIES_FILE_GATEKEEPER, PROPERTY_NAME_ORIGIN_LOG_DB);
	//private static final String ORIGIN_DATA_ACCESS_NODE = Utils.loadPropertyString(PROPERTIES_FILE_GATEKEEPER, PROPERTY_NAME_ORIGIN_DATA_ACCESS_NODE);

	private ClientAuthenticationService clientAuthenticationService = new ClientAuthenticationService(ORIGIN_AUTHENTICATION_API);
	//private ClientLogDb clientLogDb = new ClientLogDb(ORIGIN_LOG_DB);
	//private ClientDataAccessNode clientDataAccessNode = new ClientDataAccessNode(ORIGIN_DATA_ACCESS_NODE);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleOspDataAccessRequest(DtoGateKeeperRequest wrapper)
	{
		// Read the relevant details from the DTO.
		String serviceTicket = wrapper.getServiceTicket();

		// Variables for the HTTP response that could be passed back to the OSP.
		Status status = Status.OK;
		DtoGateKeeperResponse responseDto = new DtoGateKeeperResponse();

		// Check the OSP's credentials with the AS module.
		boolean isOspAuthenticated = clientAuthenticationService.isOspAuthenticated(serviceTicket);

		if (isOspAuthenticated)
		{
			// TODO - implement new plan.
		}
		else
		{
			// If the authentication was unsuccessful, return an unauthorised code.
			status = Status.UNAUTHORIZED;
		}

		// Build up the response.
		ResponseBuilder responseBuilder = Response.status(status);
		if (status == Status.OK)
		{
			responseBuilder.entity(responseDto);
		}

		// Return the response.
		logAccessRequest(wrapper, status);
		return responseBuilder.build();
	}

	/**
	 * Log details on for information on the GK.
	 * 
	 * @param wrapper
	 *        object containing information about the incoming request.
	 * @param status
	 *        indicates the decision on whether or not access should be granted.
	 */
	private void logAccessRequest(DtoGateKeeperRequest wrapper, Status status)
	{
		String message = "";
		if (status == Status.FORBIDDEN)
		{
			message = "Unauthenticated: " + wrapper;
		}
		else if (status == Status.UNAUTHORIZED)
		{
			message = "Access denied: " + wrapper;
		}
		else if (status == Status.OK)
		{
			message = "Access granted: " + wrapper;
		}
		LOGGER.info(message);
	}
}
