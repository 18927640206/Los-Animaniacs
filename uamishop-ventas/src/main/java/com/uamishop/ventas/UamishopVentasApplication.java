package com.uamishop.ventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.uamishop.ventas", "com.uamishop.shared"})
public class UamishopVentasApplication {
    public static void main(String[] args) {
        SpringApplication.run(UamishopVentasApplication.class, args);
    }
}