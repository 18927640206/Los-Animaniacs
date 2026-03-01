package com.uamishop.catalogo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.uamishop.catalogo.controller.dto.CategoriaRequest;
import com.uamishop.catalogo.controller.dto.CategoriaResponse;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // [cite: 798]
class CategoriaControllerIntegrationTest {

    private static final String BASE_URL = "/api/v2/categorias";

    @Autowired
    private TestRestTemplate restTemplate; // [cite: 802]

    @Nested
    @DisplayName("POST /api/v2/categorias") // [cite: 812, 813]
    class CrearCategoria {

        @Test
        @DisplayName("Debe crear una categoría y retornar 201") // [cite: 816]
        void crear_Retorna201() {
            // Preparar el DTO de petición
            CategoriaRequest requestBody = new CategoriaRequest();
            requestBody.setNombre("Libros");
            requestBody.setDescripcion("Categoría de prueba");

            HttpEntity<CategoriaRequest> request = new HttpEntity<>(requestBody); // [cite: 818]

            // Invocar el endpoint
            ResponseEntity<CategoriaResponse> response = restTemplate.exchange(
                BASE_URL, HttpMethod.POST, request, CategoriaResponse.class); // [cite: 820, 822]

            // Validaciones [cite: 823, 825]
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Libros", response.getBody().getNombre());
            assertNotNull(response.getBody().getId());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el nombre está vacío")
        void crear_NombreVacio_Retorna400() {
            CategoriaRequest requestBody = new CategoriaRequest();
            requestBody.setNombre(""); // Invalido [cite: 879]

            HttpEntity<CategoriaRequest> request = new HttpEntity<>(requestBody);

            ResponseEntity<ApiError> response = restTemplate.exchange(
                BASE_URL, HttpMethod.POST, request, ApiError.class);

            // Valida que el GlobalExceptionHandler esté funcionando [cite: 726, 774]
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Bad Request", response.getBody().getError());
        }
    }
} 