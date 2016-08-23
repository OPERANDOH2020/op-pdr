package eu.operando.pdr.gatekeeper;

import java.util.Vector;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.operando.ClientOperandoModuleExternal;
import eu.operando.pdr.gatekeeper.message.DtoRightsManagementOspQuery;

public class GateKeeperClient extends ClientOperandoModuleExternal
							  implements GateKeeperClientI
{
	private static final Logger LOGGER = LogManager.getLogger(GateKeeperClient.class);
	
	private String originRightsManagement = "";
	private String originDataAccessNode = "";

	public GateKeeperClient(String originAuthenticationApi, String originLogDb,String originRightsManagement,
			String originDataAccessNode)
	{
		super(originAuthenticationApi, originLogDb);
		originRightsManagement = "http://server02tecnalia.westeurope.cloudapp.azure.com:8102/operando/core/rm";
		this.originRightsManagement = originRightsManagement;
		this.originDataAccessNode = originDataAccessNode;
	}
	
	/* (non-Javadoc)
	 * @see eu.operando.pdr.gatekeeper.GateKeeperClientI#isQueryPermissible(int, java.util.Vector)
	 */
	@Override
	public AuthorisationWrapper authoriseOsp(String ospId, String roleId, String queryId, Vector<String> userIds)
	{
		AuthorisationWrapper authorisationWrapper = null;
		
		//Create a web target for the correct end-point.
		WebTarget target = getClient().target(originRightsManagement);
		target = target.path(ENDPOINT_RIGHTS_MANAGEMENT_RESULTS);

		//Send the request.
		Builder requestBuilder = target.request();
		//DtoRightsManagementOspQuery transferObject = new DtoRightsManagementOspQuery(ospId, roleId, queryId, userIds);
		DtoRightsManagementOspQuery transferObject = new DtoRightsManagementOspQuery("ST-65-s11gwmilyl3zXemlMEFV-casdotoperandodoteu", "Doctor", "FoodCoach", "{\"queryId\":\"FoodCoach;1\",\"params\":[\"matt.gallagher\"]}");
		String strJson = createStringJsonFollowingOperandoConventions(transferObject);
		Response response = requestBuilder.post(Entity.entity(strJson, MediaType.APPLICATION_JSON_TYPE));
		
		logResponseInfo("RM", response);
		
		//Interpret the response.
		if (response.getStatus() == Status.OK.getStatusCode())
		{
			String strBody = response.readEntity(String.class);
			//authorisationWrapper = createObjectFromJsonFollowingOperandoConventions(strBody, AuthorisationWrapper.class);
			authorisationWrapper = new AuthorisationWrapper(true, "secureToken");
		}
		// TODO - handle other status codes.
		return authorisationWrapper;
	}

	/* (non-Javadoc)
	 * @see eu.operando.pdr.gatekeeper.GateKeeperClientI#getDanUrlForQuery(int, java.util.Vector)
	 */
	@Override
	public String getDanUrlForQuery(String ospId, String roleId, String queryId, Vector<String> userIds)
	{
		String danUrl = "";
		
		//Create a web target for the correct end-point.
		WebTarget target = getClient().target(originDataAccessNode);
		target = target.path(ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY);

		//Send the request.
		Builder requestBuilder = target.request();
		//DtoRightsManagementOspQuery transferObject = new DtoRightsManagementOspQuery(ospId, roleId, queryId, userIds);
		DtoRightsManagementOspQuery transferObject = new DtoRightsManagementOspQuery("ST-65-s11gwmilyl3zXemlMEFV-casdotoperandodoteu", "Doctor", "FoodCoach", "{\"queryId\":\"FoodCoach;1\",\"params\":[\"5\"]}");
		String strJson = createStringJsonFollowingOperandoConventions(transferObject);
		Response response = requestBuilder.post(Entity.entity(strJson, MediaType.APPLICATION_JSON_TYPE));
		
		logResponseInfo("DAN", response);
		
		//Interpret the response.
		danUrl = response.readEntity(String.class);
		
		return danUrl;
	}

	/**
	 * Log some information about the response the client has received.
	 * 
	 * @param nameOfRespondingModule 
	 * 	the name of the module the HTTP request has come from, to display in the logs.
	 * @param response
	 * 	the response to extract information from.
	 */
	public void logResponseInfo(String nameOfRespondingModule, Response response)
	{
		LOGGER.info("Response from " + nameOfRespondingModule + ": "
				+ "\nstatus: " + response.getStatus());
				//TODO - can't read body here as readEntity closes stream for next read.
				//+ "\nbody: " + response.readEntity(String.class));
	}
}
