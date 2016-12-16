package eu.operando.pdr.dan.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.LogApi;
import io.swagger.client.model.LogRequest;
import io.swagger.client.model.LogRequest.*;

@Service
public class LogDBService implements LogDBServiceIF{
	private static final Logger LOGGER = Logger.getLogger( LogDBService.class.getName() );
	
	private static final String REQUESTER_ID = "op-pdr-dan";
	
	@Value("${logdb.url}")
	private String LOG_ENDPOINT;
	
	LogApi logApi = new LogApi();
	
	//This constructor is ONLY used for automation tests
	public void setup(String LOG_ENDPOINT) {
		this.LOG_ENDPOINT = LOG_ENDPOINT;
		setup();
	}
	
	@PostConstruct
	public void setup() {
		ApiClient apiClient =  new ApiClient().setBasePath(LOG_ENDPOINT);        
		logApi.setApiClient(apiClient);
	}	
	
	@Override
	public void log(LogDataTypeEnum logLevel, String logTitle, String logDescription, LogPriorityEnum logPriority) {		
		try {
			logApi.lodDB(new LogRequest().logPriority(logPriority).description(logDescription).requesterId(REQUESTER_ID).title(logTitle).requesterType(LogRequest.RequesterTypeEnum.MODULE).logDataType(logLevel));			
		} catch (ApiException ex) {	
			LOGGER.error(String.format("eu.operando.pdr.dan.service.LogDBService.log('%s', '%s', '%s', '%s', '%s') failed", logLevel.toString(), logTitle, logDescription, logPriority), ex);			
		}
	}
}
