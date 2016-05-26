/*
 # Copyright (c) 2016 {UPRC}.
 # All rights reserved. This program and the accompanying materials
 # are made available under the terms of the The MIT License (MIT).
 # which accompanies this distribution, and is available at
 # http://opensource.org/licenses/MIT

 # Contributors:
 #    {Constantinos Patsakis} {UPRC}
 # Initially developed in the context of OPERANDO EU project www.operando.eu 
 */
package eu.operando.pdr.dan.controller.dto;

import io.swagger.annotations.ApiModelProperty;

public class getResultsRequest {
    @ApiModelProperty(name = "st", value = "A valid service ticket (st) to access DAN", required = true, dataType = "string")
    private String st;
    
    @ApiModelProperty(name = "queryId", value = "The unique identifier of the query being called", required = true, dataType = "string")
    private String queryId;
    
    @ApiModelProperty(name = "params", value = "An array of extra parameters for the query", required = false, dataType = "Array[string]")
    
    private String[] params;
    public String getSt() {
        return st;
    }
    public void setSt(String st) {
        this.st = st;
    }
    public String getQueryId() {
        return queryId;
    }
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }
    public String[] getParams() {
        return params;
    }
    public void setParams(String[] params) {
        this.params = params;
    }
}
