package com.srikanth.clinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                            .apiInfo(apiInfo())
                            .select()
                            .apis(RequestHandlerSelectors.any())
                            .paths(PathSelectors.any())
                            .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Srikanth Kakumanu", "www.srikanth.com", "srikanth@srikanth.com");
        
		return new ApiInfoBuilder().title("Clinica API")
				.description("Clinica API reference for developers")
				.termsOfServiceUrl("http://localhost:9091")
                .contact(contact)
				.license("Clinica License")
				.licenseUrl("srikanth@srikanth.com").version("1.0").build();
	}
}
