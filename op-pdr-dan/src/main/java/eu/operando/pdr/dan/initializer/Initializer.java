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

package eu.operando.pdr.dan.initializer;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import eu.operando.pdr.dan.config.RootConfiguration;
import eu.operando.pdr.dan.config.WebConfiguration;

public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
	return new Class[] { RootConfiguration.class };	
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
	return new Class[] { WebConfiguration.class };
    }

    @Override
    protected String[] getServletMappings() {
	return new String[] { "/" };
    }

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//	super.onStartup(servletContext);
//	servletContext.setInitParameter("spring.profiles.active", "development"); //{production, development}
//    }
}