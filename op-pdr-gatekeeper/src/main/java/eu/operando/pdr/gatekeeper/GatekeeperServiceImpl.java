package eu.operando.pdr.gatekeeper;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.OperandoAuthenticationException;
import eu.operando.moduleclients.ClientRightsManagement;

/**
 * Implements the logic required of the GK.
 * 
 * Shielding this class from HTTP might be a better code design, but significantly increases the complexity required for the GK code as a whole. Since
 * the GK is currently acting as a simple proxy to the DAN, code simplicity was favoured to a loosely-coupled design. Implementation of more
 * complicated logic will likely require a complete rewrite anyway.
 *
 */
public class GatekeeperServiceImpl implements GatekeeperService
{
	private static final Logger LOGGER = LogManager.getLogger(GatekeeperServiceImpl.class);
	
	// The name of the header which contains the service ticket from the caller.
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";
	private static final String HEADER_NAME_HOST = "host";
	
	private ClientRightsManagement clientRightsManagement = null;

	public GatekeeperServiceImpl(ClientRightsManagement clientRightsManagement)
	{
		this.clientRightsManagement = clientRightsManagement;
	}

	public Response processRequest(String pathPlus, String httpMethod, HttpHeaders headersFromCaller, MultivaluedMap<String, String> queryParameters, String body, String idOspUser)
	{
		Response response;
		
		MultivaluedMap<String, String> headersToDan = filterHeaders(headersFromCaller);
		try
		{
			response = clientRightsManagement.sendRequest(httpMethod, headersToDan, idOspUser, pathPlus, queryParameters, body);
		}
		catch (OperandoAuthenticationException e)
		{
			LOGGER.error("Error with authentication procedure", e);
			response = Response.serverError().build();
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
