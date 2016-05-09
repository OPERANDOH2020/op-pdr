package eu.operando.pdr.gatekeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
//import org.glassfish.jersey.test.JerseyTest;

import eu.operando.ClientOperandoModule;

/**
 * TODO
 */
@SuppressWarnings("unused")
public class GateKeeperWebServiceTests //extends JerseyTest
{
	/*GateKeeperClientStub clientStub = new GateKeeperClientStub();
	GateKeeperWebService webService = new GateKeeperWebService(clientStub);

	@Override
	protected Application configure() {
        return new GateKeeperWebService(clientStub);
    }
	
	@Test
	public void JerseyTest()
	{
		WebTarget target = target(ClientOperandoModule.PATH_EXTERNAL_OPERANDO_GATEKEEPER).path("/data_request");
		Builder requestBuilder = target.request();
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		requestBuilder.post(null);
	}
	
	@Test
	@Ignore
	public void testProcessDataAccessRequest_OspUnauthenticated_ReturnsStatusCodeUnauthorised()
	{
		//Set up
		clientStub.setOspAuthenticated(false);

		//Exercise
		Response response = webService.handleOspDataAccessRequest(1, "", 2, new Vector<Integer>());

		//Verify
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(HttpStatus.SC_UNAUTHORIZED)));
	}
	
	@Test
	@Ignore
	public void testProcessDataAccessRequest_OspAuthenticated_QueryNotAllowed_ReturnsStatusCodeForbidden()
	{
		//Set up
		clientStub.setOspAuthenticated(true);
		clientStub.setQueryPermissible(false);

		//Exercise
		Response response = webService.handleOspDataAccessRequest(1, "", 2, new Vector<Integer>());

		//Verify
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(HttpStatus.SC_FORBIDDEN)));
	}
	
	@Test
	@Ignore
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
		Response response = webService.handleOspDataAccessRequest(1, "", 2, new Vector<Integer>());

		//Verify
		//Check the status code.
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(HttpStatus.SC_OK)));
		
		//Check the message body.
		GateKeeperResponse responseDtoActual = response.readEntity(GateKeeperResponse.class);
		GateKeeperResponse responseDtoExpected = new GateKeeperResponse();
		responseDtoExpected.setDanUrl(danUrl);
		responseDtoExpected.setSecurityToken(securityToken);
		boolean isDtoAsExpected = EqualsBuilder.reflectionEquals(responseDtoActual, responseDtoExpected);
		assertTrue("The web service should pass back the security token and DAN URL it receives from the client.", isDtoAsExpected);
	}*/
}
