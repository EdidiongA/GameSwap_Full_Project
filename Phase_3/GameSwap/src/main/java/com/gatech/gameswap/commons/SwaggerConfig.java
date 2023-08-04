package com.gatech.gameswap.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	//http://localhost:8080/GameSwap/swagger-ui/index.html#/
	@Bean
	public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2) 
				.apiInfo(apiInfo())
				.select() 
				.apis(RequestHandlerSelectors.basePackage("com.gatech.gameswap"))
				.paths(PathSelectors.any())                   
				.build();                                           
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("GameSwap")
				.description("GameSwap REST API reference")
				.version("1.0")
				.build();
	}
}
