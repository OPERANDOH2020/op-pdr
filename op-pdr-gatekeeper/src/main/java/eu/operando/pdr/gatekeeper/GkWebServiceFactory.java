package eu.operando.pdr.gatekeeper;

import eu.operando.CredentialsOperando;
import eu.operando.Utils;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoClient;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;
import eu.operando.moduleclients.ClientDataAccessNode;
import eu.operando.moduleclients.RequestBuilderAuthenticationApi;
import eu.operando.moduleclients.http.HttpRequestBuilderAuthenticationApi;

public class GkWebServiceFactory
{
	// Location of properties file.
	private static final String PROPERTIES_FILE_GK = "config.properties";

	// Property file property names.
	private static final String PROPERTY_NAME_ORIGIN_AUTHENTICATION_API = "originAuthenticationApi";
	private static final String PROPERTY_NAME_ORIGIN_DATA_ACCESS_NODE = "originDataAccessNode";
	private static final String PROPERTY_NAME_USERNAME_GK = "usernameGk";
	private static final String PROPERTY_NAME_PASSWORD_GK = "passwordGk";

	private GkWebServiceFactory()
	{
		// Factory with static method for creating GkWebServices. Should not be instantiated.
	}

	public static GkWebService create()
	{
		String originAuthenticationApi = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_ORIGIN_AUTHENTICATION_API);
		String originDataAccessNode = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_ORIGIN_DATA_ACCESS_NODE);
		ClientAuthenticationApiOperandoService clientAuthenticationApiOperandoService = new ClientAuthenticationApiOperandoService(originAuthenticationApi);

		String usernameGk = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_USERNAME_GK);
		String passwordGk = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_PASSWORD_GK);
		CredentialsOperando credentials = new CredentialsOperando(usernameGk, passwordGk);
		RequestBuilderAuthenticationApi requestBuilderAuthenticatoinApi = new HttpRequestBuilderAuthenticationApi(originAuthenticationApi, credentials);
		ClientAuthenticationApiOperandoClient clientAuthenticationServiceOperandoClient = new ClientAuthenticationApiOperandoClient(requestBuilderAuthenticatoinApi);
		ClientDataAccessNode clientDataAccessNode = new ClientDataAccessNode(originDataAccessNode, clientAuthenticationServiceOperandoClient);

		return new GkWebServiceImpl(clientAuthenticationApiOperandoService, clientDataAccessNode);
	}
}