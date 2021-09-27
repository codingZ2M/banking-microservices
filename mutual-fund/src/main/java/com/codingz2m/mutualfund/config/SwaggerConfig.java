package com.codingz2m.mutualfund.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/* :: Spring Boot RESTful API Documentation with Swagger 2 ::
  We expose APIs as a back-end component for the front-end component or third-party app integrations.
   Building a back-end API-Swagger makes documenting your RESTful services much easier.
 */

/* Configuring Auto Generation for Swagger-generated API documentation 
   Swagger 2 is enabled through the @EnableSwagger2 annotation.
   After the Docket bean is defined, its select() method returns an instance of ApiSelectorBuilder,
  which provides a way to control the end points exposed by Swagger.
 */

/*
  Swagger UI: Swagger UI is a built-in solution which makes user interaction with
  the Swagger-generated API documentation much easier.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	//Creating Docket Bean to configure Swagger2 for your Spring Boot application4
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2);
				
	} 

}


/* Documentation: 
   1.) http://localhost:8080/v2/api-docs
          OR
  2.) http://localhost:8080/swagger-ui.html
 
 Note:
  springfox.documentation.spring.web.plugins.Docket
 A builder which is intended to be the primary interface into the swagger-springmvc framework.
  Provides sensible defaults and convenience methods for configuration.
 */

