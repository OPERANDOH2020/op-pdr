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
package eu.operando.pdr.dan.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@ComponentScan( basePackages="eu.operando.pdr.dan" )
@Configuration
public class RootConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcTemplate getJdbcTemplate() {
	return new JdbcTemplate(dataSource);
    }
}
