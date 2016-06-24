package eu.operando.pdr.gatekeeper;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper class which contains the information returned from the RM module.
 */
public class AuthorisationWrapper
{
	@SerializedName("CanRunQuery")
	private boolean queryPermissible = false;
	@SerializedName("SecurityToken")
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
