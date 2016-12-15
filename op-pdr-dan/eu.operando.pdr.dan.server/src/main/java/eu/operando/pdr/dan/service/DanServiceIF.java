package eu.operando.pdr.dan.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.operando.pdr.dan.exception.AuthenticationServiceException;
import eu.operando.pdr.dan.exception.DanServiceException;
import eu.operando.pdr.dan.exception.RepositoryManagerRegistryException;

public interface DanServiceIF {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws RepositoryManagerRegistryException, AuthenticationServiceException, DanServiceException; 
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws RepositoryManagerRegistryException, AuthenticationServiceException, DanServiceException;
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws RepositoryManagerRegistryException, AuthenticationServiceException, DanServiceException;
	
	public void doPatch(HttpServletRequest request, HttpServletResponse response);
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response);
}
