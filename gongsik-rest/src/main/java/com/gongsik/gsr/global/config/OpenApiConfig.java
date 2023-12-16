package com.gongsik.gsr.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	  @Bean
	    public GroupedOpenApi customOpenAPI() {
	        return GroupedOpenApi.builder()
	                .group("api")
	                .pathsToMatch("/api/**") // API 경로 지정
	                .build();
	    }
}
