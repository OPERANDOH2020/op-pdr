package eu.operando.pdr.gatekeeper;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * XmlRootElement so that it can be converted to JSON (via xml) ussing JAX-B.
 */
@XmlRootElement
public class GateKeeperRequestWrapper
{
	private String serviceTicket = "";
	private int ospId = -1;
	private int queryId = -1;
	private Vector<Integer> userIds = new Vector<Integer>();
	
	/**
	 * Zero-arg constructor for JAX-B
	 */
	public GateKeeperRequestWrapper()
	{
		
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
	public int getOspId()
	{
		return ospId;
	}
	public void setOspId(int ospId)
	{
		this.ospId = ospId;
	}
	public int getQueryId()
	{
		return queryId;
	}
	public void setQueryId(int queryId)
	{
		this.queryId = queryId;
	}
	public Vector<Integer> getUserIds()
	{
		return userIds;
	}
	public void setUserIds(Vector<Integer> userIds)
	{
		this.userIds = userIds;
	}
}
