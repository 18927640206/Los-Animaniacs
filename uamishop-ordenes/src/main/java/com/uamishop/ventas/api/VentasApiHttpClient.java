package com.uamishop.ventas.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Implementación de VentasApi que consume el microservicio de Ventas vía HTTP (REST).
 * Esto reemplaza la antigua llamada local que hacía OrdenService al módulo de ventas en el monolito.
 */
@Component
public class VentasApiHttpClient implements VentasApi {

    private final RestTemplate restTemplate;
    private final String ventasBaseUrl;

    public VentasApiHttpClient(RestTemplate restTemplate,
                               @Value("${ventas.service.url:http://localhost:8082}") String ventasBaseUrl) {
        this.restTemplate = restTemplate;
        this.ventasBaseUrl = ventasBaseUrl;
    }

    @Override
    public CarritoResumen obtenerResumen(UUID carritoId) {
        String url = ventasBaseUrl + "/api/v2/carritos/" + carritoId + "/resumen"; // Ajusta esta ruta si en tu controller es diferente Practica 8
        
        ResponseEntity<CarritoResumen> response = restTemplate.getForEntity(url, CarritoResumen.class);
        
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("No se pudo obtener el resumen del carrito vía HTTP");
        }
        
        return response.getBody();
    }

    @Override
    public void completarCheckout(UUID carritoId) {
        // En el Paso 3 cambiamos esto para que sea asíncrono vía RabbitMQ.
        // Si OrdenService ya no llama a este método directamente, se puede dejar vacio
        // o lanzar una excepción para asegurar que nadie lo use de forma síncrona.
        throw new UnsupportedOperationException("El checkout ahora se completa de forma asíncrona vía RabbitMQ");
    }
}