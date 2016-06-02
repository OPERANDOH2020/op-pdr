package eu.operando.pdr.gatekeeper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Vector;

import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.junit.Test;

import eu.operando.ClientOperandoModuleExternalTests;

public class GateKeeperClientTests extends ClientOperandoModuleExternalTests
{

	private GateKeeperClient client = new GateKeeperClient(PROTOCOL_AND_HOST_HTTP_LOCALHOST, PROTOCOL_AND_HOST_HTTP_LOCALHOST,
			PROTOCOL_AND_HOST_HTTP_LOCALHOST, PROTOCOL_AND_HOST_HTTP_LOCALHOST);
	
	/**
	 * Authentication API
	 */
	@Test
	public void testAuthoriseOsp_CorrectHttpRequest()
	{
		testAuthoriseOsp_CorrectHttpRequest(client);
	}
	@Test
	public void testAuthoriseOsp_HandleValidTicketCorrectly()
	{
		testAuthoriseOsp_HandleValidTicketCorrectly(client);
	}
	@Test
	public void testAuthoriseOsp_HandleInvalidTicketCorrectly()
	{
		testAuthoriseOsp_HandleInvalidTicketCorrectly(client);
	}
	
	/**
	 * Rights Management
	 */
	@Test
	public void testIsQueryPermissible_CorrectHttpRequest()
	{
		//Set up
		int ospId = 1;
		int queryId = 2;
		Vector<Integer> userIds = new Vector<Integer>();
		userIds.add(3);
		userIds.add(4);
		
		//Exercise
		client.authoriseOsp(ospId, queryId, userIds);
				
		//Verify
		OspQueryTransferObject transferObject = new OspQueryTransferObject(ospId, queryId, userIds);
		verifyCorrectHttpRequestWithoutQueryParams(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, transferObject);
	}
	@Test
	public void testIsQueryPermissible_HandleQueryAllowedCorrectly()
	{
		//Set up
		int ospId = 1;
		int queryId = 2;
		Vector<Integer> userIds = new Vector<Integer>();
		userIds.add(3);
		userIds.add(4);
		String strSecurityTokenExpected = "securityToken";
		stub(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, strSecurityTokenExpected, HttpStatus.SC_OK);
		
		//Exercise
		AuthorisationWrapper authorisationWrapper = client.authoriseOsp(ospId, queryId, userIds);
		
		//Verify
		boolean isQueryPermissible = authorisationWrapper.isQueryPermissible();
		assertThat(isQueryPermissible, is(true));
		
		String securityTokenActual = authorisationWrapper.getSecurityToken();
		assertThat(securityTokenActual, is(equalTo(strSecurityTokenExpected)));
	}
	@Test
	public void testIsQueryPermissible_HandleQueryNotAllowedCorrectly()
	{
		//Set up
		int ospId = 1;
		int queryId = 2;
		Vector<Integer> userIds = new Vector<Integer>();
		userIds.add(3);
		userIds.add(4);
		stub(HttpMethod.POST, ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR, "", HttpStatus.SC_FORBIDDEN);
		
		//Exercise
		AuthorisationWrapper authorisationWrapper = client.authoriseOsp(ospId, queryId, userIds);
		
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
		int ospId = 1;
		int queryId = 2;
		Vector<Integer> userIds = new Vector<Integer>();
		userIds.add(3);
		userIds.add(4);
		
		//Exercise
		client.getDanUrlForQuery(ospId, queryId, userIds);
		
		//Verify
		OspQueryTransferObject transferObject = new OspQueryTransferObject(ospId, queryId, userIds);
		verifyCorrectHttpRequestWithoutQueryParams(HttpMethod.POST, ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY, transferObject);
	}
	@Test
	public void testGetDanUrlForQuery_HandleResponseCorrectly()
	{
		//Set up
		int ospId = 1;
		int queryId = 2;
		Vector<Integer> userIds = new Vector<Integer>();
		userIds.add(3);
		userIds.add(4);
		String danUrlExpected = "ABC-123";
		stub(HttpMethod.POST, ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY, danUrlExpected, HttpStatus.SC_OK);
		
		//Exercise
		String danUrlActual = client.getDanUrlForQuery(ospId, queryId, userIds);
		
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
