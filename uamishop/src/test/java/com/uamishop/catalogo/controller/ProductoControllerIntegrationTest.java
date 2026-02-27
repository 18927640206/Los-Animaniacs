package com.uamishop.catalogo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uamishop.catalogo.controller.dto.ProductoRequest;
import com.uamishop.catalogo.controller.dto.ProductoResponse;
import com.uamishop.shared.api.ApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoControllerIntegrationTest {

    private static final String BASE_URL = "/api/productos";

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("POST /api/productos")
    class CrearProducto {

        @Test
        @DisplayName("Debe crear un producto y retornar 201")
        void crear_Retorna201() {
            // 1. Preparar datos (asegúrate de usar una CategoriaId que exista en tu BD de prueba)
            ProductoRequest requestBody = new ProductoRequest();
            requestBody.setNombre("Laptop Gamer");
            requestBody.setDescripcion("Alta gama");
            requestBody.setPrecio(new BigDecimal("1500.00"));
            requestBody.setCategoriaId("alguna-categoria-id"); // Cambia esto por un ID real

            HttpEntity<ProductoRequest> request = new HttpEntity<>(requestBody);

            // 2. Invocar endpoint
            ResponseEntity<ProductoResponse> response = restTemplate.exchange(
                BASE_URL, HttpMethod.POST, request, ProductoResponse.class);

            // 3. Validar
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Laptop Gamer", response.getBody().getNombre());
            assertNotNull(response.getBody().getId());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los datos son inválidos")
        void crear_DatosInvalidos_Retorna400() {
            ProductoRequest requestBody = new ProductoRequest();
            requestBody.setNombre(""); // Inválido: Nombre en blanco
            requestBody.setPrecio(new BigDecimal("-10.00")); // Inválido: Precio negativo

            HttpEntity<ProductoRequest> request = new HttpEntity<>(requestBody);

            ResponseEntity<ApiError> response = restTemplate.exchange(
                BASE_URL, HttpMethod.POST, request, ApiError.class);

            // Valida que el GlobalExceptionHandler detecte las violaciones de @Valid
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }
}