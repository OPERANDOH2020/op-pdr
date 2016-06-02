package eu.operando.pdr.gatekeeper;

/**
 * A wrapper class which contains the information returned from the RM module.
 */
public class AuthorisationWrapper
{
	private boolean queryPermissible = false;
	private String securityToken = "";

	public AuthorisationWrapper(boolean queryPermissible, String securityToken)
	{
		this.queryPermissible = queryPermissible;
		this.securityToken = securityToken;
	}
	
	public boolean isQueryPermissible()
	{
		return queryPermissible;
	}
	public String getSecurityToken()
	{
		return securityToken;
	}
}
