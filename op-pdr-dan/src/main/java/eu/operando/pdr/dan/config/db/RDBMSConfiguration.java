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
package eu.operando.pdr.dan.config.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@PropertySource(value = { "classpath:rdbms-config.properties" })
public class RDBMSConfiguration {

    @Autowired
    private Environment config;

    @Bean
    @Profile("prod")
    public DataSource getProductionDataSource() {
	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	dataSource.setDriverClassName(config.getRequiredProperty("jdbc.driverClassName"));
	dataSource.setUrl(config.getRequiredProperty("jdbc.url"));
	dataSource.setUsername(config.getRequiredProperty("jdbc.username"));
	dataSource.setPassword(config.getRequiredProperty("jdbc.password"));        
	return dataSource;
    }

    @Bean    
    @Profile("dev")
    public DataSource dataSource() {

	EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
	EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.DERBY).addScript("db/sql/create-db.sql").addScript("db/sql/insert-data.sql").build();
	return db;

    }    
}
