package eu.operando.pdr.gatekeeper;

import eu.operando.CredentialsOperando;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoClient;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;
import eu.operando.moduleclients.ClientDataAccessNode;
import eu.operando.moduleclients.RequestBuilderAuthenticationApi;
import eu.operando.moduleclients.http.HttpRequestBuilderAuthenticationApi;

public class GkWebServiceFactory
{
	private GkWebServiceFactory()
	{
		// Factory with static method for creating GkWebServices. Should not be instantiated.
	}
	
	public static GkWebService create()
	{
		String originAuthenticationService = "https://snf-706921.vm.okeanos.grnet.gr:8443";
		String originDataAccessNode = "http://localhost:8080";
		ClientAuthenticationApiOperandoService clientAuthenticationApiOperandoService = new ClientAuthenticationApiOperandoService(originAuthenticationService);
		
		String usernameGk = "operando";
		String passwordGk = "1234";
		CredentialsOperando credentials = new CredentialsOperando(usernameGk, passwordGk);
		RequestBuilderAuthenticationApi requestBuilderAuthenticatoinApi = new HttpRequestBuilderAuthenticationApi(originAuthenticationService, credentials);
		ClientAuthenticationApiOperandoClient clientAuthenticationServiceOperandoClient = new ClientAuthenticationApiOperandoClient(requestBuilderAuthenticatoinApi);
		ClientDataAccessNode clientDataAccessNode = new ClientDataAccessNode(originDataAccessNode, clientAuthenticationServiceOperandoClient);
		
		return new GkWebServiceImpl(clientAuthenticationApiOperandoService, clientDataAccessNode);
	}
}
