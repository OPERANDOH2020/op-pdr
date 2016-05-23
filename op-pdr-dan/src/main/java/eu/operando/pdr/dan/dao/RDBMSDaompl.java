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
package eu.operando.pdr.dan.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("RDBMSDao")
@PropertySource(value={"classpath:sql-queries.properties"})
public class RDBMSDaompl implements RDBMSDao{
    
    @Autowired
    private Environment queries;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;        
    
    public List<Map<String, Object>> getResults(String queryId, String[] params) {	
	return jdbcTemplate.queryForList(queries.getProperty(queryId), params);
    }
}
