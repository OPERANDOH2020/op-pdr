package eu.operando.pdr.gatekeeper.message;

import com.google.gson.annotations.SerializedName;

public class DtoRightsManagementOspQuery
{
	private String ticket = "";
	@SerializedName("RoleID")
	private String roleId = "";
	@SerializedName("ServiceID")
	private String serviceId = "";
	@SerializedName("ParamValues")
	private String paramValues = "";
	
	public DtoRightsManagementOspQuery(String ticket, String roleId, String serviceId, String paramValues)
	{
		super();
		this.ticket = ticket;
		this.roleId = roleId;
		this.serviceId = serviceId;
		this.paramValues = paramValues;
	}
}
