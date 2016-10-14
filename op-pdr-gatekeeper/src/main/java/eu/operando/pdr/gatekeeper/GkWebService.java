package eu.operando.pdr.gatekeeper;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public interface GkWebService
{
	Response processRequest(String pathPlus, String httpMethod, HttpHeaders headers);
	Response processRequest(String pathPlus, String httpMethod, HttpHeaders headers, String body);
}
