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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.operando.pdr.dan.exception.AuthenticationServiceException;
import eu.operando.pdr.dan.exception.DanServiceException;
import eu.operando.pdr.dan.exception.RepositoryManagerRegistryException;
import eu.operando.pdr.dan.service.DanServiceIF;


@RestController
@RequestMapping(value="/**")
public class DanController {
	private static final Logger LOGGER = Logger.getLogger( DanController.class.getName() );

	@Autowired
	DanServiceIF service;

	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		//GBE adding monitor capability
		String path=request.getPathInfo();
		System.out.print("Path: " + path);
		if (path=="/monitor")
		{
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("server ok");
			return;
		}
		//GBE end

		try {
			service.doGet(request, response);
		} catch(RepositoryManagerRegistryException | AuthenticationServiceException | DanServiceException e){
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;
			}catch(IOException ioe){
				LOGGER.error(ioe.getMessage());
			}
		}		
	}

	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response){

		try {
			service.doPost(request, response);
		} catch(RepositoryManagerRegistryException | AuthenticationServiceException | DanServiceException e){
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;
			}catch(IOException ioe){
				LOGGER.error(ioe.getMessage());
			}
		}		
	}

	@RequestMapping(method=RequestMethod.PUT)
	public void doPut(HttpServletRequest request, HttpServletResponse response){

		try {
			service.doPut(request, response);
		} catch(RepositoryManagerRegistryException | AuthenticationServiceException | DanServiceException e){
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;
			}catch(IOException ioe){
				LOGGER.error(ioe.getMessage());
			}
		}		
	}
}
