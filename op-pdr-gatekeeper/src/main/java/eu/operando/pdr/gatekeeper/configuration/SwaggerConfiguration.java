package eu.operando.pdr.gatekeeper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.swagger.annotations.SwaggerDefinition;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** 
 * @author sg
 * @since 4.1.5
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "eu.operando.pdr.gk")
@EnableSwagger2
class SwaggerConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseRegisteredSuffixPatternMatch(true);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer
		.favorPathExtension(false)
		.favorParameter(false)		
		.ignoreAcceptHeader(false)
		.useJaf(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("json", MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("html", MediaType.TEXT_HTML);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2)  
				.select()                                  
				.apis(RequestHandlerSelectors.any())              
				.paths(PathSelectors.any())                          
				.build()
				.apiInfo(apiInfo());                                           
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				// TODO update this
				"OPERANDO's Authentication Service's API (AAPI)",
				"A Restful API provided by OPERANDO's AS for authentication/authorization",
				"1.0.0",
				"termsOfService = \"share and care\"",
				"@Contact(name = \"Patsakis-Costas\", email = \"kpatsak@gmail.com\", url = \"\")",
				"license = @License(name = \"Apache 2.0\", url = \"http://www.apache.org\"",          
				null);        
		return apiInfo;
	}
}
