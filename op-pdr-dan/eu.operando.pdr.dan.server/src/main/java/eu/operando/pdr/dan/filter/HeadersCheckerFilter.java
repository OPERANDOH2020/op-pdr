package eu.operando.pdr.dan.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import eu.operando.pdr.dan.constants.CustomHttpHeaders;

public class HeadersCheckerFilter implements Filter{
	private static final String SC_BAD_REQUEST_DESC = "One or more of the requered headers are missing (service-ticket, osp-identifier,  psp-user-identifier) ";
	private static final Logger LOGGER = Logger.getLogger( HeadersCheckerFilter.class.getName() ); //GBE

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//GBE adding monitor capability
		String path=((HttpServletRequest)request).getRequestURI();
		LOGGER.debug("HeadersCheckerFilter-Path: " + path);
		if (path.matches("\\/monitor$"))
		{
			chain.doFilter(request, response);
			return;
		}
		//GBE end
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (httpRequest.getHeader(CustomHttpHeaders.SERVICE_TICKET.toString()) == null ||
				httpRequest.getHeader(CustomHttpHeaders.OSP_IDENTIFIER.toString()) == null ||
				httpRequest.getHeader(CustomHttpHeaders.PSP_USER_IDENTIFIER.toString()) == null ) {			
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, SC_BAD_REQUEST_DESC);
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
}
