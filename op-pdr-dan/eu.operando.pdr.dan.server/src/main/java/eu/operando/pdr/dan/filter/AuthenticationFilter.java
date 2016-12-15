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

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import eu.operando.pdr.dan.constants.CustomHttpHeaders;
import eu.operando.pdr.dan.service.AuthenticationService;

public class AuthenticationFilter implements Filter{

	private static final String SERVICE_ID="op-pdr/dan";

	private AuthenticationService authenticationService;

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String serviceTicket = ((HttpServletRequest)request).getHeader(CustomHttpHeaders.SERVICE_TICKET.toString());

		if (!authenticationService.isServiceTicketValid(serviceTicket, SERVICE_ID)){
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig cfg) { 
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(cfg.getServletContext());
		this.authenticationService = ctx.getBean(AuthenticationService.class);
	}

}
