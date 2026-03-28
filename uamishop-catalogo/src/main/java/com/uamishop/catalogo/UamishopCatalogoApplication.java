// Archivo: /workspaces/Los-Animaniacs/uamishop-catalogo/src/main/java/com/uamishop/catalogo/UamishopCatalogoApplication.java

package com.uamishop.catalogo;

/*ES IMPORTANTE YA QUE PARA PODER HACER LO DE LA P4 REQUERIAMOS DE SPRINGBOOT. 
YA CONE ESTO SE PUEDE USAR @Valid, @RestController,@ControllerAdvice*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling

public class UamishopCatalogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UamishopCatalogoApplication.class, args);
    }
}