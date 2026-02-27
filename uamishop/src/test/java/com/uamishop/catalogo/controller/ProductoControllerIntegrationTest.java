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

import com.uamishop.catalogo.controller.dto.CategoriaRequest;
import com.uamishop.catalogo.controller.dto.CategoriaResponse;
import com.uamishop.catalogo.repository.CategoriaJpaRepository;
//import com.uamishop.catalogo.domain.CategoriaId;
import com.uamishop.shared.domain.CategoriaId;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoControllerIntegrationTest {

    private static final String PRODUCTOS_URL = "/api/productos";
    private static final String CATEGORIAS_URL = "/api/categorias";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoriaJpaRepository categoriaRepository;

    private String categoriaIdValido;

    @BeforeEach
    void setUp() {
        // Crear una categoría válida antes de cada test
        CategoriaRequest catRequest = new CategoriaRequest();
        catRequest.setNombre("Categoria test");
        catRequest.setDescripcion("description test");
        
        ResponseEntity<CategoriaResponse> catResponse = restTemplate.postForEntity(
        "/api/v1/categorias", catRequest, CategoriaResponse.class);
            
        assertEquals(HttpStatus.CREATED, catResponse.getStatusCode());
        assertNotNull(catResponse.getBody());
        assertNotNull(catResponse.getBody().getId());

        //Guardar el ID
        this.categoriaId = catResponse.getBody().getId();
    }

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
            requestBody.setCategoriaId(categoriaIdValido); // Cambia esto por un ID real

            HttpEntity<ProductoRequest> request = new HttpEntity<>(requestBody);

            ResponseEntity<ProductoResponse> response = restTemplate.exchange(
                PRODUCTOS_URL, HttpMethod.POST, request, ProductoResponse.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Laptop Gamer", response.getBody().getNombre());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los datos son inválidos")
        void crear_DatosInvalidos_Retorna400() {
            ProductoRequest requestBody = new ProductoRequest();
            requestBody.setNombre(""); // Inválido: Nombre en blanco
            requestBody.setPrecio(new BigDecimal("-10.00")); // Inválido: Precio negativo

            HttpEntity<ProductoRequest> request = new HttpEntity<>(requestBody);

            ResponseEntity<ApiError> response = restTemplate.exchange(
                PRODUCTOS_URL, HttpMethod.POST, request, ApiError.class);

            // Valida que el GlobalExceptionHandler detecte las violaciones de @Valid
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }
}