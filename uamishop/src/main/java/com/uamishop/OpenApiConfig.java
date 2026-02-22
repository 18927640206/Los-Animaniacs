package com.uamishop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
  
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mi uamishop API")         
                        .version("1.0")                       
                        .description("API de nuestra tienda uamishop para la gestión de productos, órdenes y ventas") 
                        .contact(new Contact()
                                .name("<Los Animaniacs")   
                                .url("https://uamishop.com")         
                                .email("animaniacs409@izt.uam.mx"))   
                        .license(new License()
                                .name("Apache 2.0")         
                                .url("http://springdoc.org")));   
    }
}