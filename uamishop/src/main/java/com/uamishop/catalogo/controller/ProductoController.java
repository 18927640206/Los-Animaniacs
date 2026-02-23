package com.uamishop.catalogo.controller;

import jakarta.validation.Valid;    
import com.uamishop.catalogo.controller.dto.ProductoRequest;
import com.uamishop.catalogo.controller.dto.ProductoResponse;
import com.uamishop.catalogo.service.ProductoService;
import com.uamishop.shared.api.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Endpoints para gestión de productos en el catálogo")
public class ProductoController {

    private final ProductoService productoService;
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Registra un producto en el catálogo con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            headers = @Header(
                name = "Location",
                description = "URI del recurso creado (/api/productos/{id})",
                schema = @Schema(type = "string", format = "uri")
            ),
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (error de validación o producto duplicado)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener producto por ID",
        description = "Detalles de un producto específico según su identificador único (UUID)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = ProductoResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "ID inválido (no es UUID)"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoResponse> obtenerProducto(
        @Parameter(description = "Identificador único del producto (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping
    @Operation(
        summary = "Listar todos los productos",
        description = "Retorna una lista de todos los productos registrados en el catálogo."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoResponse.class, type = "array")
            )
        )
    })
    public ResponseEntity<List<ProductoResponse>> listar() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar un producto existente",
        description = "Actualiza los datos de un producto existente identificado por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado correctamente",
            content = @Content(schema = @Schema(implementation = ProductoResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoResponse> actualizar(
            @Parameter(description = "Identificador único del producto (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto del catálogo según su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado (sin contenido)"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "Identificador único del producto (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
