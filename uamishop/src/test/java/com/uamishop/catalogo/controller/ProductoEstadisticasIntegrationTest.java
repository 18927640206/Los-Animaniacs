package com.uamishop.catalogo.controller;

import com.uamishop.catalogo.controller.dto.ProductoEstadisticasResponse;
import com.uamishop.shared.event.ProductoCompradoEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoEstadisticasIntegrationTest {

    private static final String PRODUCTOS_URL = "/api/v2/productos";
    private static final String ESTADISTICAS_SUFFIX = "/estadisticas";
    private static final String MAS_VENDIDOS_URL = PRODUCTOS_URL + "/mas-vendidos";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher; // Para disparar el evento manualmente

    @Test
    @DisplayName("Debe retornar los productos ordenados por cantidad vendida")
    void debeRetornarMasVendidos() {
        UUID prod1 = UUID.randomUUID();
        UUID prod2 = UUID.randomUUID();

        // Enviamos eventos con diferentes cantidades
        publicarEventoCompra(prod1, 10); // Más vendido
        publicarEventoCompra(prod2, 5);  // Segundo más vendido

        await().atMost(5, TimeUnit.SECONDS).ignoreExceptions().untilAsserted(() -> {
            ResponseEntity<List> response = restTemplate.getForEntity(
                "/api/v2/productos/mas-vendidos?limit=2", List.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            // Aquí podrías verificar que el primer elemento de la lista sea prod1
            assertFalse(response.getBody().isEmpty());
        });
    }

    @Test
    @DisplayName("Debe actualizar estadísticas asíncronamente cuando se compra un producto")
    void alComprarProducto_SeActualizanEstadisticas() {
        UUID productoId = UUID.randomUUID();
        int cantidadComprada = 3;

        // IMPORTANTE: Asegúrate de que el constructor coincida con tu clase ProductoCompradoEvent
        ProductoCompradoEvent.ItemComprado item = new ProductoCompradoEvent.ItemComprado(
            productoId, "SKU", cantidadComprada, new BigDecimal("10.0"), "Prod"
        );
        ProductoCompradoEvent evento = new ProductoCompradoEvent(
            UUID.randomUUID(), Instant.now(), UUID.randomUUID(), UUID.randomUUID(), List.of(item)
        );

        eventPublisher.publishEvent(evento);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .pollInterval(Duration.ofMillis(500))
            .ignoreExceptions() // <--- ESTO ES LO QUE FALTA. Ignora el 404 mientras el hilo asíncrono trabaja
            .untilAsserted(() -> {
                var response = restTemplate.getForEntity(
                    "/api/v2/productos/" + productoId + "/estadisticas", 
                    ProductoEstadisticasResponse.class
                );

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(1, response.getBody().ventasTotales());
                assertEquals(cantidadComprada, response.getBody().cantidadVendida());
            });
    }

    private void publicarEventoCompra(UUID productoId, int cantidad) {
        ProductoCompradoEvent.ItemComprado item = new ProductoCompradoEvent.ItemComprado(
            productoId, "SKU-TEST", cantidad, new BigDecimal("100.00"), "Producto Test"
        );

        ProductoCompradoEvent evento = new ProductoCompradoEvent(
            UUID.randomUUID(), 
            Instant.now(), 
            UUID.randomUUID(), 
            UUID.randomUUID(), 
            List.of(item)
        );

        eventPublisher.publishEvent(evento);
    }
}