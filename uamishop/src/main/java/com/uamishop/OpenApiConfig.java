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
                        .title("API de UamiShop")
                        .version("1.0")
                        .description("Documentación del API REST para el sistema de ventas UamiShop")
                        .contact(new Contact()
                                .name("Los Animaniacs")
                                .email("tu-correo@uam.mx"))
                        .license(new License()
                                .name("API License")
                                .url("https://www.uam.mx")));
    }
}