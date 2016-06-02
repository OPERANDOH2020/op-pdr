package eu.operando.pdr.gatekeeper;

import java.util.Vector;

/**
 * TODO - move when unit testing is possible. 
 */
public class GateKeeperClientStub implements GateKeeperClientI
{
	private boolean isOspAuthenticated = false;
	private boolean queryPermissible = false;
	private String securityToken = "";
	private String danUrl = "";

	public GateKeeperClientStub()
	{
		
	}
	public GateKeeperClientStub(boolean isOspAuthenticated, boolean queryPermissible, String securityToken,
			String danUrl)
	{
		this.isOspAuthenticated = isOspAuthenticated;
		this.queryPermissible = queryPermissible;
		this.securityToken = securityToken;
		this.danUrl = danUrl;
	}
	
	@Override
	public boolean isOspAuthenticated(String serviceTicket)
	{
		return isOspAuthenticated;
	}
	@Override
	public AuthorisationWrapper authoriseOsp(int ospId, int queryId, Vector<Integer> userIds)
	{
		return new AuthorisationWrapper(queryPermissible, securityToken);
	}
	@Override
	public String getDanUrlForQuery(int ospId, int queryId, Vector<Integer> userIds)
	{
		return danUrl ;
	}
	public void setOspAuthenticated(boolean isOspAuthenticated)
	{
		this.isOspAuthenticated = isOspAuthenticated;
	}
	public void setQueryPermissible(boolean queryPermissible)
	{
		this.queryPermissible = queryPermissible;
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
