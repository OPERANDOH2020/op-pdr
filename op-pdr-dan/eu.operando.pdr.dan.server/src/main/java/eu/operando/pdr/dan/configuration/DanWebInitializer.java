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

package eu.operando.pdr.dan.configuration;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import eu.operando.pdr.dan.filter.AuthenticationFilter;
import eu.operando.pdr.dan.filter.HeadersCheckerFilter;
import eu.operando.pdr.dan.filter.ResponsibleCheckerFilter;

public class DanWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	 
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { DanAppConfig.class, HttpClientConfiguration.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { DanWebConfig.class };
    }
  
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

	@Override
	protected Filter[] getServletFilters() {		
		return new Filter[] {new ResponsibleCheckerFilter(), new HeadersCheckerFilter(), new AuthenticationFilter()};
	}
}
