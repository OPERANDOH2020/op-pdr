package eu.operando.pdr.dan.service;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.operando.pdr.dan.constants.CustomHttpHeaders;
import eu.operando.pdr.dan.exception.AuthenticationServiceException;
import eu.operando.pdr.dan.exception.DanServiceException;
import eu.operando.pdr.dan.exception.RepositoryManagerRegistryException;
import eu.operando.pdr.dan.registry.RepositoryManager;
import eu.operando.pdr.dan.registry.RepositoryManagersRegistry;
import eu.operando.pdr.dan.utils.Helper;
import io.swagger.client.model.LogRequest;

@Service
public class DanService implements DanServiceIF {
	private static final Logger LOGGER = Logger.getLogger( DanService.class.getName() );

	@Autowired
	RepositoryManagersRegistry repositoryManagersRegistry;

	@Autowired
	AuthenticationServiceIF authenticationService;

	@Autowired
	LogDBServiceIF remoteLogger;

	@Autowired
	CloseableHttpClient httpClient;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws RepositoryManagerRegistryException, AuthenticationServiceException, DanServiceException {
		RepositoryManager rm = getRepositoryManager(request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()));

		String relativePath = request.getRequestURI().substring(request.getContextPath().length());
		String queryParams = request.getQueryString()!=null ? "?" + request.getQueryString() : null;
		String uri = getRepositoryManagerURI(rm, relativePath, queryParams);

		HttpGet httpget=null;
		CloseableHttpResponse  result=null;
		try{
			httpget = new HttpGet(uri);

			filterCallerHeaders(request, httpget, rm.getHost());

			//add appropriate service-ticket header
			addServiceTicketHeader(httpget, rm.getServiceId());
			
			result= httpClient.execute(httpget);

			HttpEntity entity = result.getEntity();
//			TODO: Following snippet consumes the stream. Another solution is needed.
//			if (LOGGER.isDebugEnabled()) {
//				BufferedReader r = new BufferedReader(new InputStreamReader(entity.getContent()));
//				StringBuilder total = new StringBuilder();
//				String line = null;
//				while ((line = r.readLine()) != null) {
//					total.append(line);
//				}
//				LOGGER.debug(total.toString());
//			}

			if (entity != null) {				
				Helper.stream(entity.getContent(), response.getOutputStream());
			}

		} catch (IOException ex){
			LOGGER.error("eu.operando.pdr.dan.service.DanService.doGet(...) failed", ex);
			throw new DanServiceException(-1, ex.getMessage());
		} finally{
			try {
				if (result!=null) {
					result.close();
				}
			} catch (IOException ioe) {
				LOGGER.error(ioe.getMessage());
			}                        
		}		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws RepositoryManagerRegistryException, AuthenticationServiceException, DanServiceException {
		RepositoryManager rm = getRepositoryManager(request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()));

		String relativePath = request.getRequestURI().substring(request.getContextPath().length());
		String queryParams = request.getQueryString()!=null ? "?" + request.getQueryString() : "";
		String uri = getRepositoryManagerURI(rm, relativePath, queryParams);
		
		HttpPost httppost=null;
		CloseableHttpResponse  result=null;
		try{
			httppost = new HttpPost(uri);

			filterCallerHeaders(request, httppost, rm.getHost());

			//add appropriate service-ticket header
			addServiceTicketHeader(httppost, rm.getServiceId());
						
			//add caller body
			BufferedReader reader = request.getReader();
			StringBuffer json = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null){
				json.append(line);
			}
			
			StringEntity params = new StringEntity(json.toString());
//			params.setContentType("application/json; charset=UTF-8");
//			httppost.addHeader("content-type", "application/json;odata=verbose");
			httppost.setEntity(params);

			result= httpClient.execute(httppost);

			HttpEntity entity = result.getEntity();
//			if (LOGGER.isDebugEnabled()) {TODO: Find a way to log without consuming the stream
//				BufferedReader r = new BufferedReader(new InputStreamReader(entity.getContent()));
//				StringBuilder total = new StringBuilder();
//				line = null;
//				while ((line = r.readLine()) != null) {
//					total.append(line);
//				}
//				LOGGER.debug(total.toString());
//			}

			if (entity != null) {				
				Helper.stream(entity.getContent(), response.getOutputStream());
			}

		} catch (IOException ex){
			LOGGER.error("eu.operando.pdr.dan.service.DanService.doGet(...) failed", ex);
			throw new DanServiceException(-1, ex.getMessage());
		} finally{
			try {
				if (result!=null) {
					result.close();
				}
			} catch (IOException ioe) {
				LOGGER.error(ioe.getMessage());
			}                        
		}		
	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws RepositoryManagerRegistryException, AuthenticationServiceException, DanServiceException {
		RepositoryManager rm = getRepositoryManager(request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()));

		String relativePath = request.getRequestURI().substring(request.getContextPath().length());
		String queryParams = request.getQueryString()!=null ? "?" + request.getQueryString() : "";
		String uri = getRepositoryManagerURI(rm, relativePath, queryParams);
		
		HttpPut httpput=null;
		CloseableHttpResponse  result=null;
		try{
			httpput = new HttpPut(uri);

			filterCallerHeaders(request, httpput, rm.getHost());

			//add appropriate service-ticket header
			addServiceTicketHeader(httpput, rm.getServiceId());

			//add caller body
			BufferedReader reader = request.getReader();
			StringBuffer json = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null){
				json.append(line);
			}
			
			StringEntity params = new StringEntity(json.toString());
//			params.setContentType("application/json; charset=UTF-8");
//			httpput.addHeader("content-type", "application/json;odata=verbose");
			httpput.setEntity(params);

			result= httpClient.execute(httpput);

			HttpEntity entity = result.getEntity();
//			if (LOGGER.isDebugEnabled()) {TODO: Find a way to log without consuming the stream
//				BufferedReader r = new BufferedReader(new InputStreamReader(entity.getContent()));
//				StringBuilder total = new StringBuilder();
//				line = null;
//				while ((line = r.readLine()) != null) {
//					total.append(line);
//				}
//				LOGGER.debug(total.toString());
//			}

			if (entity != null) {				
				Helper.stream(entity.getContent(), response.getOutputStream());
			}

		} catch (IOException ex){
			LOGGER.error("eu.operando.pdr.dan.service.DanService.doGet(...) failed", ex);
			throw new DanServiceException(-1, ex.getMessage());
		} finally{
			try {
				if (result!=null) {
					result.close();
				}
			} catch (IOException ioe) {
				LOGGER.error(ioe.getMessage());
			}                        
		}		
	}

	@Override
	public void doPatch(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException();

	}

	private RepositoryManager getRepositoryManager(String name) throws RepositoryManagerRegistryException {		
		RepositoryManager rm = repositoryManagersRegistry.get(name);
		if (rm == null){			
			remoteLogger.log(LogRequest.LogLevelEnum.WARN, "", "The requested OSP [" + name + "] not found.", LogRequest.LogPriorityEnum.NORMAL);
			throw new RepositoryManagerRegistryException(HttpServletResponse.SC_NOT_FOUND, "The requested OSP [" + name + "] not found.");
		}

		return rm;
	}

	private String getRepositoryManagerURI(RepositoryManager rm, String relativePath, String queryParams) {
		String URI = rm.getSchema().toString() + "://" +  rm.getHost() + ":" + rm.getPort() + rm.getServiceRoot() + relativePath + (queryParams!=null ? "?" + queryParams : "");
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug(String.format("eu.operando.pdr.dan.service.DanService.getRepositoryManagerURI returned '%s'", URI));
		}

		return URI;
	}

	private void addServiceTicketHeader(HttpRequestBase request, String serviceId) throws AuthenticationServiceException {

		String serviceTicket = authenticationService.issueServiceTicket(serviceId);

		request.addHeader(CustomHttpHeaders.SERVICE_TICKET.toString(), serviceTicket);
	}
	
	private void filterCallerHeaders(HttpServletRequest callerRequest, HttpRequestBase request, String resourceHost) throws AuthenticationServiceException {
		boolean contentTypeExists = false;
		
		Enumeration<String> headerNames = callerRequest.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String name = (String)headerNames.nextElement();
			if ("host".equalsIgnoreCase(name)) {//Below it will be replaced by the resource being requested (https://tools.ietf.org/html/rfc2616#section-14.23)
				continue;				
			} else if ("content-length".equalsIgnoreCase(name)) { //This is added by the HttpClient based on the body of the HttpPost/HttpPut
				continue;
			} else if ("content-type".equalsIgnoreCase(name)) { ////Below it will be setup to 'application/json'. This is only supported by DAN
				continue;
			} else if (CustomHttpHeaders.SERVICE_TICKET.toString().equalsIgnoreCase(name)) { //filter out that header. we have to generate a new one 
				continue;
			}


			Enumeration<String> headerValues = callerRequest.getHeaders(name);
			while(headerValues.hasMoreElements()) {
				String value = (String)headerValues.nextElement();
				request.addHeader(name, value);					
			}				
		}

		//add HOST header set to the resource being requested (https://tools.ietf.org/html/rfc2616#section-14.23))
		request.addHeader("host", resourceHost);
		
		if ((request instanceof HttpPost) || (request instanceof HttpPut) || (request instanceof HttpPatch)) {
			request.addHeader("content-type", "application/json");				
		}

	}
}
