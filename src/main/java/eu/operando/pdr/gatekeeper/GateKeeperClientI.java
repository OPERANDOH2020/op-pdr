package eu.operando.pdr.gatekeeper;

import java.util.Vector;

public interface GateKeeperClientI
{
	/**
	 * Check with the Authentication Service that the OSP is authenticated. 
	 */
	public boolean isOspAuthenticated(String serviceTicket);
	
	/**
	 * Check with the RM module that a query should be allowed to run on the DB.
	 */
	public AuthorisationWrapper authoriseOsp(int ospId, int queryId, Vector<Integer> userIds);

	/**
	 * Ask a DAN which DAN can provide access to the requested data.
	 */
	public String getDanUrlForQuery(int ospId, int queryId, Vector<Integer> userIds);

}