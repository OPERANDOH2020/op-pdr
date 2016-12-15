package eu.operando.pdr.dan.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.operando.pdr.dan.exception.AuthenticationServiceException;
import io.swagger.client.ApiClient;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.UserCredential;

@Service
public class AuthenticationService implements AuthenticationServiceIF{
	private static final Logger LOGGER = Logger.getLogger( AuthenticationService.class.getName() );

	private DefaultApi aapi = new DefaultApi();
	
	@Value("${as.url}")
	private String AS_ENDPOINT;
	
	@Value("${as.username}")
	private String username;
	
	@Value("${as.password}")
	private String password;
	
	private String tgt = null;
	
	//This  is ONLY used for automation tests
	public void setup(String AS_ENDPOINT, String username, String password){
		this.AS_ENDPOINT =AS_ENDPOINT ; 
		this.username = username ; 
		this.password =password ; 
		setup();
	}
	
	@PostConstruct
	public void setup() {
		ApiClient apiClient =  new ApiClient().setBasePath(AS_ENDPOINT);        
		aapi.setApiClient(apiClient);		
	}	
			
	@Override
	public void issueTicketGrantigTicket(String username, String password) throws AuthenticationServiceException {
		try{
			
			UserCredential credentials= new UserCredential();
			credentials.setUsername(username.replaceAll("\"", ""));
			credentials.setPassword(password.replaceAll("\"", ""));
			
			this.tgt = aapi.aapiTicketsPost(credentials);
			if (LOGGER.isDebugEnabled()){								
				LOGGER.debug(String.format("User with username '%s' and password '%s' issued tgt '%s'", username, password, tgt));
			}			
		}catch(Exception ex){
			LOGGER.error(String.format("eu.operando.pdr.dan.service.AuthenticationService.issueTicketGrantigTicket(%s, %s) failed", username, password), ex);
			throw new AuthenticationServiceException(-1, ex.getMessage());
		}				
	}

	@Override
	public String issueServiceTicket(String serviceId) throws AuthenticationServiceException{
		
		try{
			if (tgt == null){
				synchronized (AuthenticationService.class) {
					if (tgt == null){
						issueTicketGrantigTicket(this.username, this.password);		
					}
				}
			}
			return aapi.aapiTicketsTgtPost(tgt, serviceId);	
		}catch(Exception ex){
			LOGGER.error(String.format("eu.operando.pdr.dan.service.AuthenticationService.issueServiceTicket(%s) failed", serviceId), ex);			
			return issueServiceTicketWithNewTGT(serviceId);
		}
	}
		
	private String issueServiceTicketWithNewTGT(String serviceId) throws AuthenticationServiceException{ //TODO: is needed better synchronization
		try{
			issueTicketGrantigTicket(this.username, this.password);
			return aapi.aapiTicketsTgtPost(tgt, serviceId);	
		}catch(Exception ex){
			LOGGER.error(String.format("eu.operando.pdr.dan.service.AuthenticationService.issueServiceTicketWithNewTGT(%s) failed", serviceId), ex);
			throw new AuthenticationServiceException(-2, ex.getMessage());
		}
	}

	@Override
	public boolean isServiceTicketValid(String st, String serviceId) {
		
		try{
			aapi.aapiTicketsStValidateGet(st, serviceId);	
		}catch(Exception ex){
			LOGGER.error(String.format("eu.operando.pdr.dan.service.AuthenticationService.isServiceTicketValid(%s, %s) failed", st, serviceId), ex);
			return false;
		}
		
		return true;
	}
}
