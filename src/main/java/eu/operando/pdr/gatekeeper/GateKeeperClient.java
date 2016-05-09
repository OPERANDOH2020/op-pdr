package eu.operando.pdr.gatekeeper;

import java.util.Vector;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import eu.operando.ClientOperandoModuleExternal;

public class GateKeeperClient extends ClientOperandoModuleExternal
							  implements GateKeeperClientI
{
	private String protocolAndHostRightsManagement = "";
	private String protocolAndHostDataAccessNode = "";

	public GateKeeperClient(String protocolAndHostAuthenticationApi, String protocolAndHostLogDb,String protocolAndHostRightsManagement,
			String protocolAndHostDataAccessNode)
	{
		super(protocolAndHostAuthenticationApi, protocolAndHostLogDb);
		this.protocolAndHostRightsManagement = protocolAndHostRightsManagement;
		this.protocolAndHostDataAccessNode = protocolAndHostDataAccessNode;
	}
	
	/* (non-Javadoc)
	 * @see eu.operando.pdr.gatekeeper.GateKeeperClientI#isQueryPermissible(int, java.util.Vector)
	 */
	@Override
	public AuthorisationWrapper authoriseOsp(int ospId, int queryId, Vector<Integer> userIds)
	{
		boolean permissible = false;
		String securityToken = "";
		
		//Create a web target for the correct end-point.
		WebTarget target = getClient().target(protocolAndHostRightsManagement);
		target = target.path(ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR);

		//Send the request.
		Builder requestBuilder = target.request();
		OspQueryTransferObject transferObject = new OspQueryTransferObject(ospId, queryId, userIds);
		String strJson = createStringJsonFollowingOperandoConventions(transferObject);
		Response response = requestBuilder.post(Entity.entity(strJson, MediaType.APPLICATION_JSON_TYPE));
		
		//Interpret the response.
		if (response.getStatus() == HttpStatus.SC_OK)
		{
			permissible = true;
			securityToken = response.readEntity(String.class);
		}
		
		return new AuthorisationWrapper(permissible, securityToken);
	}

	/* (non-Javadoc)
	 * @see eu.operando.pdr.gatekeeper.GateKeeperClientI#getDanUrlForQuery(int, java.util.Vector)
	 */
	@Override
	public String getDanUrlForQuery(int ospId, int queryId, Vector<Integer> userIds)
	{
		String danUrl = "";
		
		//Create a web target for the correct end-point.
		WebTarget target = getClient().target(protocolAndHostDataAccessNode);
		target = target.path(ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY);

		//Send the request.
		Builder requestBuilder = target.request();
		OspQueryTransferObject transferObject = new OspQueryTransferObject(ospId, queryId, userIds);
		String strJson = createStringJsonFollowingOperandoConventions(transferObject);
		Response response = requestBuilder.post(Entity.entity(strJson, MediaType.APPLICATION_JSON_TYPE));

		//Interpret the response.
		danUrl = response.readEntity(String.class);
		
		return danUrl;
	}

}
