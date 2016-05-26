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
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import eu.operando.pdr.dan.config.db.RDBMSConfiguration;

@ComponentScan(basePackages = {"eu.operando.pdr.dan.dao", "eu.operando.pdr.dan.service"})
@Configuration
@Import(RDBMSConfiguration.class)
public class RootConfiguration {

    @Bean
    @Autowired
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
	return new JdbcTemplate(dataSource);
    }
}
