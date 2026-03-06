package com.uamishop;

/*ES IMPORTANTE YA QUE PARA PODER HACER LO DE LA P4 REQUERIAMOS DE SPRINGBOOT. 
YA CONE ESTO SE PUEDE USAR @Valid, @RestController,@ControllerAdvice*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;//EnableAsync P6 2.4
//EnableAsync P6 2.4
@EnableAsync
@SpringBootApplication
public class UamishopApplication {

    public static void main(String[] args) {
        SpringApplication.run(UamishopApplication.class, args);
    }
}