package eu.operando.pdr.gatekeeper;

/**
 * Wrapper class to contain response details. 
 */
public class GateKeeperResponse
{
	private String securityToken = "";
	private String danUrl = "";
	
	public GateKeeperResponse() {}

	
	public String getSecurityToken()
	{
		return securityToken;
	}
	public String getDanUrl()
	{
		return danUrl;
	}
	public void setSecurityToken(String securityToken)
	{
		this.securityToken = securityToken;
	}
	public void setDanUrl(String danUrl)
	{
		this.danUrl = danUrl;
	}
}
