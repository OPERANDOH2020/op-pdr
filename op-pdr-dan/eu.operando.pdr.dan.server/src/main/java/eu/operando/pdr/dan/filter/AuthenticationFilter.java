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

import eu.operando.pdr.dan.constants.CustomHttpHeaders;
import eu.operando.pdr.dan.utils.Helper;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;

public class AuthenticationFilter implements Filter{
	
	private static final String SERVICE_ID="op-pdr/dan";
	private static final String BASE_URL=Helper.DAN_PROPS.getProperty("aapi.url");
	
	private DefaultApi aapi = new DefaultApi();

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String st = ((HttpServletRequest)request).getHeader(CustomHttpHeaders.SERVICE_TICKET.toString());
		try{
			aapi.aapiTicketsStValidateGet(st, SERVICE_ID);	
		}catch(ApiException apiex){			
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}		
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		aapi.getApiClient().setBasePath(BASE_URL);
	}

}
