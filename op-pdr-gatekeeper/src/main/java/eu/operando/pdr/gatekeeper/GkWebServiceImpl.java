package eu.operando.pdr.gatekeeper;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoAuthenticationException;
import eu.operando.OperandoCommunicationException;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;
import eu.operando.moduleclients.ClientDataAccessNode;

/**
 * Implements the logic required of the GK.
 * 
 * Shielding this class from HTTP might be a better code design, but significantly increases the complexity required for the GK code as a whole. Since
 * the GK is currently acting as a simple proxy to the DAN, code simplicity was favoured to a loosely-coupled design. Implementation of more
 * complicated logic will likely require a complete rewrite anyway.
 *
 */
public class GkWebServiceImpl implements GkWebService
{
	private static final String ERROR_MESSAGE_INVALID_SERVICE_TICKET = "Invalid service ticket";
	private static final Logger LOGGER = LogManager.getLogger(GkWebServiceImpl.class);
	// The ID for this service.
	private static final String SERVICE_ID_GATEKEEPER = "/gatekeeper";
	// The name of the header which contains the service ticket from the caller.
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";
	private static final String HEADER_NAME_HOST = "host";
	
	private ClientAuthenticationApiOperandoService clientAuthenticationApi = null;
	private ClientDataAccessNode clientDataAccessNode = null;

	public GkWebServiceImpl(ClientAuthenticationApiOperandoService clientAuthenticationService, ClientDataAccessNode clientDataAccessNode)
	{
		this.clientAuthenticationApi = clientAuthenticationService;
		this.clientDataAccessNode = clientDataAccessNode;
	}

	public Response processRequest(String pathPlus, String httpMethod, HttpHeaders headers)
	{
		return processRequest(pathPlus, httpMethod, headers, "");
	}

	public Response processRequest(String pathPlus, String httpMethod, HttpHeaders headersFromCaller, String body)
	{
		Response response = null;
		
		String serviceTicket = headersFromCaller.getHeaderString(HEADER_NAME_SERVICE_TICKET);
		Status status = null;
		String errorMessage = "";
		
		boolean error = false;
		try
		{
			AuthenticationWrapper authenticationWrapper = clientAuthenticationApi.requestAuthenticationDetails(serviceTicket, SERVICE_ID_GATEKEEPER);
			boolean validTicket = authenticationWrapper.isTicketValid();
			if (validTicket)
			{
				String idOspUser = authenticationWrapper.getIdOspUser();
				MultivaluedMap<String, String> headersToDan = filterHeaders(headersFromCaller);
				try
				{
					response = clientDataAccessNode.sendRequest(idOspUser, pathPlus, httpMethod, headersToDan, body);
				}
				catch (OperandoAuthenticationException e)
				{
					error = true;
					status = Status.INTERNAL_SERVER_ERROR;
					LOGGER.error("Error with authentication procedure", e);
				}
			}
			else
			{
				error = true;
				status = Status.FORBIDDEN;
				errorMessage = ERROR_MESSAGE_INVALID_SERVICE_TICKET;
			}
		}
		catch (OperandoCommunicationException e)
		{
			error = true;
			status = Status.INTERNAL_SERVER_ERROR;
			LOGGER.error("Error communicating with another module", e);
		}

		if (error)
		{
			response = Response.status(status).entity(errorMessage).build();
		}		
		
		return response;
	}

	private MultivaluedMap<String, String> filterHeaders(HttpHeaders headersFromCaller)
	{
		MultivaluedMap<String, String> headersToDan = new MultivaluedStringMap(headersFromCaller.getRequestHeaders());
		headersToDan.remove(HEADER_NAME_HOST);
		headersToDan.remove(HEADER_NAME_SERVICE_TICKET);
		return headersToDan;
	}
}
