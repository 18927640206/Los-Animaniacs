package com.uamishop.catalogo.controller;

import jakarta.validation.Valid;    
import com.uamishop.catalogo.controller.dto.ProductoRequest;
import com.uamishop.catalogo.controller.dto.ProductoResponse;
import com.uamishop.catalogo.controller.dto.ProductoEstadisticasResponse;
import com.uamishop.catalogo.service.ProductoService;
import com.uamishop.catalogo.service.ProductoEstadisticasService;
import com.uamishop.catalogo.domain.ProductoEstadisticas;
import com.uamishop.shared.api.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/productos")
@Tag(name = "Productos", description = "Endpoints para gestión de productos en el catálogo")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoEstadisticasService productoEstadisticasService;
    
    public ProductoController(ProductoService productoService, ProductoEstadisticasService productoEstadisticasService) {
        this.productoService = productoService;
        this.productoEstadisticasService = productoEstadisticasService;
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Registra un producto en el catálogo y retorna la ubicación del recurso creado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            headers = @Header(
                name = "Location",
                description = "URI del recurso creado",
                schema = @Schema(type = "string", format = "uri")
            ),
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (error de validación)",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.crear(request);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoResponse> obtenerProducto(@PathVariable UUID id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<List<ProductoResponse>> listar() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mas-vendidos")
    @Operation(
        summary = "Lista de productos ordenados por ventas",
        description = "Retorna el top de productos mas vendidos."
    )
    public ResponseEntity<List<ProductoEstadisticasResponse>> obtenerMasVendidos(
        @RequestParam(defaultValue = "10") int limit) {
            List<ProductoEstadisticasResponse> response = productoEstadisticasService.obtenerMasVendidos(limit)
            .stream()
            .map(this::mapearAResponse)
            .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<ProductoEstadisticasResponse> obtenerEstadisticas(@PathVariable UUID id) {

        ProductoEstadisticas stats = productoEstadisticasService.obtenerEstadisticas(id);

        if (stats == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapearAResponse(stats));
    }

    private ProductoEstadisticasResponse mapearAResponse(ProductoEstadisticas stats) {
        return new ProductoEstadisticasResponse(
            stats.getVentasTotales(),          
            stats.getCantidadVendida(),        
            stats.getVecesAgregadoAlCarrito(), 
            stats.getUltimaVentaAt()           
        );
    }
}
