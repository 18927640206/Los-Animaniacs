// Archivo: /workspaces/Los-Animaniacs/uamishop-ordenes/src/main/java/com/uamishop/UamishopOrdenesApplication.java

package com.uamishop;

/* ES IMPORTANTE YA QUE PARA PODER HACER LO DE LA P4 REQUERIAMOS DE SPRINGBOOT. 
YA CON ESTO SE PUEDE USAR @Valid, @RestController,@ControllerAdvice */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {"com.uamishop.ordenes", "com.uamishop.shared", "com.uamishop.ventas", "com.uamishop.catalogo"})
public class UamishopOrdenesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UamishopOrdenesApplication.class, args);
    }

    // --- IMPORTANTE PARA LA PRÁCTICA 8 ---
    // Este Bean es necesario para crear los adaptadores REST (Paso 1.1)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}