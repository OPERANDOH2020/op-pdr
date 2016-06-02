package eu.operando.pdr.gatekeeper;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class OspQueryTransferObject
{
	@SerializedName("OspID")
	private int ospId = -1;
	@SerializedName("QID")
	private int queryId = -1;
	@SerializedName("UIDs")
	private Vector<Integer> userIds = new Vector<Integer>();

	public OspQueryTransferObject(int ospId, int queryId, Vector<Integer> userIds)
	{
		this.ospId = ospId;
		this.queryId = queryId;
		this.userIds = userIds;		
	}
}
