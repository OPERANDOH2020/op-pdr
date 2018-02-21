package eu.operando.pdr.gatekeeper;

import eu.operando.CredentialsOperando;
import eu.operando.Utils;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoClient;
import eu.operando.moduleclients.ClientRightsManagement;
import eu.operando.moduleclients.RequestBuilderAuthenticationApi;
import eu.operando.moduleclients.http.HttpRequestBuilderAuthenticationApi;

public class GatekeeperServiceFactory
{
	// Location of properties file.
	private static final String PROPERTIES_FILE_GK = "config.properties";

	// Property file property names.
	private static final String PROPERTY_NAME_ORIGIN_AUTHENTICATION_API = "originAuthenticationApi";
	private static final String PROPERTY_NAME_ORIGIN_RIGHTS_MANAGEMENT = "originRightsManagement";
	private static final String PROPERTY_NAME_USERNAME_GK = "usernameGk";
	private static final String PROPERTY_NAME_PASSWORD_GK = "passwordGk";

	private GatekeeperServiceFactory()
	{
		// Factory with static method for creating GkWebServices. Should not be instantiated.
	}

	public static GatekeeperService create()
	{
		String originAuthenticationApi = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_ORIGIN_AUTHENTICATION_API);
		String originRightsManagement = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_ORIGIN_RIGHTS_MANAGEMENT);

		String usernameGk = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_USERNAME_GK);
		String passwordGk = Utils.loadPropertyString(PROPERTIES_FILE_GK, PROPERTY_NAME_PASSWORD_GK);
		CredentialsOperando credentials = new CredentialsOperando(usernameGk, passwordGk);
		RequestBuilderAuthenticationApi requestBuilderAuthenticatoinApi = new HttpRequestBuilderAuthenticationApi(originAuthenticationApi, credentials);
		ClientAuthenticationApiOperandoClient clientAuthenticationServiceOperandoClient = new ClientAuthenticationApiOperandoClient(requestBuilderAuthenticatoinApi);
		ClientRightsManagement clientRightsManagement = new ClientRightsManagement(originRightsManagement, clientAuthenticationServiceOperandoClient);

		return new GatekeeperServiceImpl(clientRightsManagement);
	}
}
