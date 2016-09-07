package eu.operando.pdr.gatekeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Vector;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.operando.moduleclients.ClientAuthenticationService;
import eu.operando.moduleclients.ClientDataAccessNode;
import eu.operando.moduleclients.ClientLogDb;
import eu.operando.pdr.gatekeeper.message.DtoGateKeeperRequest;

@RunWith(MockitoJUnitRunner.class)
public class GateKeeperWebServiceTests
{
	@Mock
	private ClientAuthenticationService clientAuthenticationService;
	
	@Mock
	private ClientLogDb clientLogDb;
	
	@Mock
	private ClientDataAccessNode clientDataAccessNode;
	
	@InjectMocks
	private GateKeeperWebService webService = new GateKeeperWebService();
	
	@Test
	public void testProcessDataAccessRequest_OspUnauthenticated_ReturnsStatusCodeUnauthorised()
	{
		//Set up
		when(clientAuthenticationService.isOspAuthenticated(anyString())).thenReturn(false);

		//Exercise
		DtoGateKeeperRequest requestWrapper = new DtoGateKeeperRequest();
		requestWrapper.setServiceTicket("");
		requestWrapper.setOspId("1");
		requestWrapper.setQueryId("2");
		requestWrapper.setUserIds(new Vector<String>());
		Response response = webService.handleOspDataAccessRequest(requestWrapper);

		//Verify
		int statusCodeReturned = response.getStatus();
		assertThat(statusCodeReturned, is(equalTo(Status.UNAUTHORIZED.getStatusCode())));
	}
}
