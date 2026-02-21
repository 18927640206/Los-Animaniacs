package com.uamishop;

/*ES IMPORTANTE YA QUE PARA PODER HACER LO DE LA P4 REQUERIAMOS DE SPRINGBOOT. 
YA CONE ESTO SE PUEDE USAR @Valid, @RestController,@ControllerAdvice*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UamishopApplication {

    public static void main(String[] args) {
        SpringApplication.run(UamishopApplication.class, args);
    }
}