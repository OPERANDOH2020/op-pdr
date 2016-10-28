/*******************************************************************************
 *  # Copyright (c) 2016 {UPRC}.
 *  # All rights reserved. This program and the accompanying materials
 *  # are made available under the terms of the The MIT License (MIT).
 *  # which accompanies this distribution, and is available at
 *  # http://opensource.org/licenses/MIT
 *
 *  # Contributors:
 *  #    {Constantinos Patsakis} {UPRC}
 *  #    {Stamatis Glykos} {UPRC}
 *  #    {Constantinos Alexandris} {UPRC}
 *  # Initially developed in the context of OPERANDO EU project www.operando.eu 
 *******************************************************************************/

package eu.operando.pdr.dan.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.operando.pdr.dan.cache.RepositoryManager;
import eu.operando.pdr.dan.cache.RepositoryManagerCache;
import eu.operando.pdr.dan.constants.CustomHttpHeaders;
import eu.operando.pdr.dan.utils.Helper;
import eu.operando.pdr.dan.utils.Log;
import io.swagger.client.model.LogRequest;
import java.util.logging.Level;
import org.apache.log4j.Logger;


@RestController
@RequestMapping(value="/**")
public class ProxyController {
	
	@Autowired
	RepositoryManagerCache cache;
	
	@Autowired
	CloseableHttpClient httpClient;
        
        Log log = new Log();
	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		
		RepositoryManager rm = cache.get(request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()));		
		if (rm == null){
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested OSP [" + request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()) + "] not found.");
				log.logMe(LogRequest.LogDataTypeEnum.WARN, "", "The requested OSP [" + request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()) + "] not found.", LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
                                return;
			} catch (IOException e) {			
				e.printStackTrace();
                                log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
                                
			}
		}
		
		String uri = rm.getSchema().toString() + "://" +  rm.getHost() + ":" + rm.getPort() + rm.getServiceRoot() + request.getRequestURI().substring(request.getContextPath().length()) + (request.getQueryString()!=null ? "?" + request.getQueryString() : "");
				
		HttpGet httpget=null;
		CloseableHttpResponse  result=null;
		try{	
			log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Opening new GET http connection for uri: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
			httpget = new HttpGet(uri);

			//add headers
			Enumeration headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()) {
				String name = (String)headerNames.nextElement();

				Enumeration headerValues = request.getHeaders(name);
				while(headerValues.hasMoreElements()) {
					String value = (String)headerValues.nextElement();
					httpget.addHeader(name, value);
				}				
			}
			
			log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Executing GET for: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			result= httpClient.execute(httpget);

			HttpEntity entity = result.getEntity();
			/*
			Debugging mode:
			BufferedReader r = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuilder total = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			System.out.println(total.toString());
			 */

			if (entity != null) {				
				Helper.stream(entity.getContent(), response.getOutputStream());
			}			
		} catch(Exception ex){
			ex.printStackTrace();
                        log.logMe(LogRequest.LogDataTypeEnum.WARN, "", ex.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
                        
		} finally{
			try {
				result.close();
			} catch (IOException e) {
                        log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
                        }
		}
                
                log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Execution of GET completed for: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
                        
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response){

		RepositoryManager rm = cache.get(request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()));		
		if (rm == null){
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested OSP [" + request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()) + "] not found.");
				return;
			} catch (IOException e) {			
				e.printStackTrace();
				log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			}
		}

		String uri = rm.getSchema().toString() + "://" +  rm.getHost() + ":" + rm.getPort() + rm.getServiceRoot() + request.getRequestURI().substring(request.getContextPath().length()) + (request.getQueryString()!=null ? "?" + request.getQueryString() : "");

		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				json.append(line);
		} catch (Exception e) { 
			e.printStackTrace();
			log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

		}		


		HttpPost httppost=null;
		CloseableHttpResponse  result=null;		
		try{			
			log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Opening new POST http connection for uri: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			httppost = new HttpPost(uri);

			//append headers
			Enumeration headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()) {
				String name = (String)headerNames.nextElement();
				if ("content-length".equals(name)){ //For some reason it should not be in the HttpPost object
					continue;
				}

				Enumeration headerValues = request.getHeaders(name);
				while(headerValues.hasMoreElements()) {					
					String value = (String)headerValues.nextElement();
					httppost.addHeader(name, value);
				}				
			}

			//append json
			StringEntity params = new StringEntity(json.toString());
			params.setContentType("application/json; charset=UTF-8");
			httppost.addHeader("content-type", "application/json;odata=verbose");
			httppost.setEntity(params);

			log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Executing POST for: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			result= httpClient.execute(httppost);

			HttpEntity entity = result.getEntity();
			/*
			Debugging mode:
			BufferedReader r = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuilder total = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			System.out.println(total.toString());
			 */

			if (entity != null) {				
				Helper.stream(entity.getContent(), response.getOutputStream());
			}			
		} catch(Exception ex){
			ex.printStackTrace();
			log.logMe(LogRequest.LogDataTypeEnum.WARN, "", ex.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");


		} finally{
			try {
				result.close();
			} catch (IOException e) {
				log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			}
		}	
		log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Execution of POST completed for: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public void doPut(HttpServletRequest request, HttpServletResponse response){

		RepositoryManager rm = cache.get(request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()));		
		if (rm == null){
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested OSP [" + request.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()) + "] not found.");
				return;
			} catch (IOException e) {			
				e.printStackTrace();
				log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			}
		}

		String uri = rm.getSchema().toString() + "://" +  rm.getHost() + ":" + rm.getPort() + rm.getServiceRoot() + request.getRequestURI().substring(request.getContextPath().length()) + (request.getQueryString()!=null ? "?" + request.getQueryString() : "");

		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				json.append(line);
		} catch (Exception e) { 
			e.printStackTrace();
			log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

		}		


		HttpPut httpput=null;
		CloseableHttpResponse  result=null;		
		try{			
			log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Opening new POST http connection for uri: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			httpput = new HttpPut(uri);

			//append headers
			Enumeration headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()) {
				String name = (String)headerNames.nextElement();
				if ("content-length".equals(name)){ //For some reason it should not be in the HttpPost object
					continue;
				}

				Enumeration headerValues = request.getHeaders(name);
				while(headerValues.hasMoreElements()) {					
					String value = (String)headerValues.nextElement();
					httpput.addHeader(name, value);
				}				
			}

			//append json
			StringEntity params = new StringEntity(json.toString());
			params.setContentType("application/json; charset=UTF-8");
			httpput.addHeader("content-type", "application/json;odata=verbose");
			httpput.setEntity(params);

			log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Executing POST for: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			result= httpClient.execute(httpput);

			HttpEntity entity = result.getEntity();
			/*
			Debugging mode:
			BufferedReader r = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuilder total = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			System.out.println(total.toString());
			 */

			if (entity != null) {				
				Helper.stream(entity.getContent(), response.getOutputStream());
			}			
		} catch(Exception ex){
			ex.printStackTrace();
			log.logMe(LogRequest.LogDataTypeEnum.WARN, "", ex.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");


		} finally{
			try {
				result.close();
			} catch (IOException e) {
				log.logMe(LogRequest.LogDataTypeEnum.WARN, "", e.toString(), LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");

			}
		}	
		log.logMe(LogRequest.LogDataTypeEnum.INFO, "", "Execution of PUT completed for: " +uri, LogRequest.LogPriorityEnum.NORMAL.toString(), "op-pdr-dan");
	}
}
