package eu.operando.pdr.gatekeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.http.HttpStatus;
import org.junit.Test;
//import org.glassfish.jersey.test.JerseyTest;

import eu.operando.pdr.gatekeeper.message.DtoGateKeeperRequest;
import eu.operando.pdr.gatekeeper.message.DtoGateKeeperResponse;

public class GateKeeperWebServiceTests
{
	GateKeeperClientStub clientStub = new GateKeeperClientStub();
	GateKeeperWebService webService = new GateKeeperWebService(clientStub);
	
	@Test
	public void testProcessDataAccessRequest_OspUnauthenticated_ReturnsStatusCodeUnauthorised()
	{
		//Set up
		clientStub.setOspAuthenticated(false);

		//Exercise
		Response response = exerciseHandleOspDataAccessRequestWithDummyRequestWrapper();

		//Verify
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(HttpStatus.SC_UNAUTHORIZED)));
	}
	
	@Test
	public void testProcessDataAccessRequest_OspAuthenticated_QueryNotAllowed_ReturnsStatusCodeForbidden()
	{
		//Set up
		clientStub.setOspAuthenticated(true);
		clientStub.setQueryPermissible(false);

		//Exercise
		Response response = exerciseHandleOspDataAccessRequestWithDummyRequestWrapper();

		//Verify
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(HttpStatus.SC_FORBIDDEN)));
	}
	
	@Test
	public void testProcessDataAccessRequest_OspAuthenticated_QueryAllowed_ReturnsStatusCodeOkDanUrlAndSecurityToken()
	{
		//Set up
		clientStub.setOspAuthenticated(true);
		clientStub.setQueryPermissible(true);
		String danUrl = "ABC-123";
		clientStub.setDanUrl(danUrl);
		String securityToken = "securityToken";
		clientStub.setSecurityToken(securityToken);

		//Exercise
		Response response = exerciseHandleOspDataAccessRequestWithDummyRequestWrapper();

		//Verify
		//Check the status code.
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(HttpStatus.SC_OK)));
		
		//Check the message body.
		DtoGateKeeperResponse responseDtoActual = response.readEntity(DtoGateKeeperResponse.class);
		DtoGateKeeperResponse responseDtoExpected = new DtoGateKeeperResponse();
		responseDtoExpected.setDanUrl(danUrl);
		responseDtoExpected.setSecurityToken(securityToken);
		boolean isDtoAsExpected = EqualsBuilder.reflectionEquals(responseDtoActual, responseDtoExpected);
		assertTrue("The web service should pass back the security token and DAN URL it receives from the client.", isDtoAsExpected);
	}

	public Response exerciseHandleOspDataAccessRequestWithDummyRequestWrapper()
	{
		DtoGateKeeperRequest requestWrapper = new DtoGateKeeperRequest();
		requestWrapper.setServiceTicket("");
		requestWrapper.setOspId("1");
		requestWrapper.setQueryId("2");
		requestWrapper.setUserIds(new Vector<String>());
		Response response = webService.handleOspDataAccessRequest(requestWrapper);
		return response;
	}
}
