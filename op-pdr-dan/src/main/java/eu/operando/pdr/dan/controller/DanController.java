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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.operando.pdr.dan.controller.exceptions.DanBaseException;
import eu.operando.pdr.dan.model.Results;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/operando/pdr/access_node/")
public class DanController {

	@RequestMapping(value = "/getResults", method = RequestMethod.POST)
	@ApiOperation( value = "getResults", 
		notes = "This operation returns in JSON format the requested rows that result by the query (given as a parameter), after the validation of the ticket (which is also given as a parameter).",
		response=Results.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 400, message = "Server error"), 
			@ApiResponse(code = 200, message = "OK", response = Results.class) 
	})
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "st", value = "A valid service ticket (st) to access DAN", required = true, dataType = "string", paramType = "body"), 
		@ApiImplicitParam(name = "queryId", value = "The unique identifier of the query being called", required = true, dataType = "string", paramType = "body"),
		@ApiImplicitParam(name = "paramValues", value = "An array of extra parameters for the query", required = false, dataType = "Array[string]", paramType = "body")
	})
		   
	public final ResponseEntity<Results> getResults(
			@RequestBody String st, 
			@RequestBody String queryId, 
			@RequestBody(required=false) final String[] paramValues) throws DanBaseException{		

		return new ResponseEntity<Results>(new Results(), HttpStatus.OK); 
	}
}