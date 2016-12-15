package eu.operando.pdr.dan.service;

import eu.operando.pdr.dan.exception.AuthenticationServiceException;

public interface AuthenticationServiceIF {
	
	public void issueTicketGrantigTicket(String username, String password) throws AuthenticationServiceException;
	
	public String issueServiceTicket(String serviceId) throws AuthenticationServiceException;
			
	public boolean isServiceTicketValid(String st, String serviceId) throws AuthenticationServiceException;
}
