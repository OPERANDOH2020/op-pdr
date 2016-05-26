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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.operando.pdr.dan.controller.dto.getResultsRequest;
import eu.operando.pdr.dan.controller.exceptions.DanBaseException;
import eu.operando.pdr.dan.service.RDBMSService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/operando/pdr/access_node/")
public class DanController {

    @Autowired
    private RDBMSService service;


    @RequestMapping(value = "/getResults", method = RequestMethod.POST)
    @ApiOperation( value = "getResults", 
    notes = "This operation returns in JSON format the requested rows that result by the query (given as a parameter), after the validation of the ticket (which is also given as a parameter)."
	    )
    @ApiResponses(value = { 
	    @ApiResponse(code = 400, message = "Server error")			 
    })      	   
    public final List<Map<String,Object>> getResults(@RequestBody getResultsRequest request) throws DanBaseException{		

	return service.getResults(request.getQueryId(), request.getParams());
    }
}