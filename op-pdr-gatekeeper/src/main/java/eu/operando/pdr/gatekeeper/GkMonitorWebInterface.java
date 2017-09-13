package eu.operando.pdr.gatekeeper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Due to changes in the work-flow for handling requests to access data, the Gatekeeper is now essentially a placeholder module (which will manage
 * additional up-front validation of requests beyond checking authentication), which currently functions as a proxy to the Data Access Node.
 * 
 */
@Path("monitor")
@RestController
@RequestMapping(value="monitor")
public class GkMonitorWebInterface
{
	@GET
	public Response processGetRequest(@Context HttpHeaders headers)
	{
		System.out.print("monitor"); //GBE
		
        return Response.ok().entity("server ok").build();
	}
	
}
