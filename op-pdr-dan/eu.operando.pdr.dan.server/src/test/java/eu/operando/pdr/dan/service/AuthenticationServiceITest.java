package eu.operando.pdr.dan.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import eu.operando.pdr.dan.exception.AuthenticationServiceException;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class AuthenticationServiceITest {
			
	@Configuration
    @PropertySource("classpath:config.properties")
    public static class Config {
		
		@Bean
		public PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
			return new PropertySourcesPlaceholderConfigurer();
		}
		
//		@Bean
//        public AuthenticationServiceIF getAuthenticationService() {
//			return new AuthenticationService();            
//        }
	}
	
	@Value("${as.url}")
	private String AS_ENDPOINT;
	
	@Value("${as.username}")
	private String username;
	
	@Value("${as.password}")
	private String password;
	
	@Autowired
	AuthenticationServiceIF service;
	
	@Before
	public void setup() {
		((AuthenticationService)service).setup(AS_ENDPOINT, username, password);
	}

	@Test
	public void issueServiceTicketForBuiltInRM() throws AuthenticationServiceException {		
		String serviceTicket = service.issueServiceTicket("op-pdr/rpm/built-in");
		Assert.notNull(serviceTicket);
	}

}
