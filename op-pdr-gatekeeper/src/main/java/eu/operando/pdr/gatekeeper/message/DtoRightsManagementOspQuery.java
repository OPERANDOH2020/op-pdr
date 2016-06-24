package eu.operando.pdr.gatekeeper.message;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class DtoRightsManagementOspQuery
{
	@SerializedName("OspId")
	private String ospId = "";
	@SerializedName("RoleId")
	private String roleId = "";
	@SerializedName("QueryId")
	private String queryId = "";
	@SerializedName("UserIds")
	private Vector<String> userIds = new Vector<String>();

	public DtoRightsManagementOspQuery(String ospId, String roleId, String queryId, Vector<String> userIds)
	{
		this.ospId = ospId;
		this.roleId = roleId;
		this.queryId = queryId;
		this.userIds = userIds;		
	}
}
