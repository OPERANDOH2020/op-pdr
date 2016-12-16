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

import io.swagger.client.model.LogRequest;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LogDBServiceITest {
	
	@Configuration
    @PropertySource("classpath:config.properties")
    public static class Config {				
		@Bean
		public PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
			return new PropertySourcesPlaceholderConfigurer();
		}
		
		@Bean
        public LogDBServiceIF getLogDBService() {
			return new LogDBService();            
        }
	}
	
	@Value("${logdb.url}")
	private String LOG_ENDPOINT;
	
	@Autowired
	LogDBServiceIF service;
	
	@Before
	public void setup(){
		((LogDBService)service).setup(LOG_ENDPOINT);
	}
	
	@Test
	public void log() {		
		service.log(LogRequest.LogDataTypeEnum.WARN, "testing", "testing", LogRequest.LogPriorityEnum.NORMAL);
	}
}
