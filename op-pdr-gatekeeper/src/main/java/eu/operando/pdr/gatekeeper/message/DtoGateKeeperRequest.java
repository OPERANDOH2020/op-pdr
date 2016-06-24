package eu.operando.pdr.gatekeeper.message;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * XmlRootElement so that it can be converted to JSON (via xml) ussing JAX-B.
 */
@XmlRootElement
public class DtoGateKeeperRequest
{
	private String serviceTicket = "";
	private String ospId = "";
	private String roleId = "";
	private String queryId = "";
	private Vector<String> userIds = new Vector<String>();
	
	/**
	 * Zero-arg constructor for JAX-B
	 */
	public DtoGateKeeperRequest()
	{	
	}
	
	@Override
	public String toString()
	{
		return "OSP: " + ospId
				+ ", role: " + roleId
				+ ", service ticket: " + serviceTicket
				+ ", query: " + queryId
				+ ", users: " + userIds;
	}

	/**
	 * gets are needed for ResponseBuilder.build()
	 */
	public String getServiceTicket()
	{
		return serviceTicket;
	}
	public void setServiceTicket(String serviceTicket)
	{
		this.serviceTicket = serviceTicket;
	}
	public String getOspId()
	{
		return ospId;
	}
	public void setOspId(String ospId)
	{
		this.ospId = ospId;
	}
	public String getRoleId()
	{
		return roleId;
	}
	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
	public String getQueryId()
	{
		return queryId;
	}
	public void setQueryId(String queryId)
	{
		this.queryId = queryId;
	}
	public Vector<String> getUserIds()
	{
		return userIds;
	}
	public void setUserIds(Vector<String> userIds)
	{
		this.userIds = userIds;
	}
}
