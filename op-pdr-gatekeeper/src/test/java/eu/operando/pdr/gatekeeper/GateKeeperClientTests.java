package eu.operando.pdr.gatekeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import eu.operando.ClientOperandoModuleExternalTests;
import eu.operando.pdr.gatekeeper.message.DtoRightsManagementOspQuery;

public class GateKeeperClientTests extends ClientOperandoModuleExternalTests
{

	private GateKeeperClient client = new GateKeeperClient(ORIGIN_WIREMOCK, ORIGIN_WIREMOCK, ORIGIN_WIREMOCK, ORIGIN_WIREMOCK);
	
	/**
	 * Authentication API
	 */
	@Test
	public void testIsOspAuthenticated_CorrectHttpRequest()
	{
		testIsOspAuthenticated_CorrectHttpRequest(client);
	}
	@Test
	public void testIsOspAuthenticated_HandleValidTicketCorrectly()
	{
		testIsOspAuthenticated_HandleValidTicketCorrectly(client);
	}
	@Test
	public void testIsOspAuthenticated_HandleInvalidTicketCorrectly()
	{
		testIsOspAuthenticated_HandleInvalidTicketCorrectly(client);
	}
	
	/**
	 * Rights Management
	 */
	@Test
	public void testAuthoriseOsp_CorrectHttpRequest()
	{
		//Set up
		String ospId = "1";
		String roleId = "2";
		String queryId = "3";
		Vector<String> userIds = new Vector<String>();
		userIds.add("4");
		userIds.add("5");
		
		stub(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, "", Status.OK.getStatusCode());
		//Exercise
		client.authoriseOsp(ospId, roleId, queryId, userIds);
				
		//Verify that the transfer object sent is as expected.
		DtoRightsManagementOspQuery transferObject = new DtoRightsManagementOspQuery("ST-65-s11gwmilyl3zXemlMEFV-casdotoperandodoteu", "Doctor", "FoodCoach", "{\"queryId\":\"FoodCoach;1\",\"params\":[\"5\"]}");
		verifyCorrectHttpRequestNoQueryParams(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, transferObject);
	}
	@Test
	public void testAuthoriseOsp_HandleQueryAllowedCorrectly()
	{
		//Set up
		String ospId = "1";
		String roleId = "2";
		String queryId = "3";
		Vector<String> userIds = new Vector<String>();
		userIds.add("4");
		userIds.add("5");
		
		String strSecurityTokenExpected = "securityToken";
		AuthorisationWrapper authorisationWrapperExpected = new AuthorisationWrapper(true, strSecurityTokenExpected);
		stub(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, authorisationWrapperExpected, Status.OK.getStatusCode());
		
		//Exercise
		AuthorisationWrapper authorisationWrapperActual = client.authoriseOsp(ospId, roleId, queryId, userIds);
		
		//Verify
		boolean isQueryPermissible = authorisationWrapperActual.isQueryPermissible();
		assertThat(isQueryPermissible, is(true));
		
		String securityTokenActual = authorisationWrapperActual.getSecurityToken();
		assertThat(securityTokenActual, is(equalTo(strSecurityTokenExpected)));
	}
	@Test
	public void testAuthoriseOsp_HandleQueryNotAllowedCorrectly()
	{
		//Set up
		String ospId = "1";
		String roleId = "2";
		String queryId = "3";
		Vector<String> userIds = new Vector<String>();
		userIds.add("4");
		userIds.add("5");
		
		AuthorisationWrapper authorisationWrapperExpected = new AuthorisationWrapper(false, "");
		stub(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, authorisationWrapperExpected, Status.OK.getStatusCode());
		
		//Exercise
		AuthorisationWrapper authorisationWrapper = client.authoriseOsp(ospId, roleId, queryId, userIds);
		
		//Verify
		boolean isQueryPermissible = authorisationWrapper.isQueryPermissible();
		assertThat(isQueryPermissible, is(false));
	}
	
	/**
	 * Data Access Node 
	 */
	@Test
	public void testGetDanUrlForQuery_CorrectHttpRequest()
	{
		//Set up
		String ospId = "1";
		String roleId = "2";
		String queryId = "3";
		Vector<String> userIds = new Vector<String>();
		userIds.add("4");
		userIds.add("5");
		
		//Exercise
		client.getDanUrlForQuery(ospId, roleId, queryId, userIds);
		
		//Verify
		DtoRightsManagementOspQuery dtoShouldSend = new DtoRightsManagementOspQuery("ST-65-s11gwmilyl3zXemlMEFV-casdotoperandodoteu", "Doctor", "FoodCoach", "{\"queryId\":\"FoodCoach;1\",\"params\":[\"5\"]}");
		verifyCorrectHttpRequestNoQueryParams(HttpMethod.POST, ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY, dtoShouldSend);
	}
	@Test
	public void testGetDanUrlForQuery_HandleResponseCorrectly()
	{
		//Set up
		String ospId = "1";
		String roleId = "2";
		String queryId = "3";
		Vector<String> userIds = new Vector<String>();
		userIds.add("4");
		userIds.add("5");
		String danUrlExpected = "ABC-123";
		stub(HttpMethod.POST, ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY, danUrlExpected, Status.OK.getStatusCode());
		
		//Exercise
		String danUrlActual = client.getDanUrlForQuery(ospId, roleId, queryId, userIds);
		
		//Verify
		assertThat(danUrlActual, is(equalTo(danUrlExpected)));
	}
	
	/**
	 * Log DB
	 */
	@Test
	public void testLogActivity_CorrectHttpRequest()
	{
		testLogActivity_CorrectHttpRequest(client);
	}
}
