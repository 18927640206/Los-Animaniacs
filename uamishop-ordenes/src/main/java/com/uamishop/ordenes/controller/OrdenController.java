package com.uamishop.ordenes.controller;

import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
import com.uamishop.ordenes.domain.Orden;
import com.uamishop.ordenes.service.OrdenService;
import com.uamishop.shared.api.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/ordenes") // Versionamiento según Práctica 5 
@Tag(name = "Órdenes", description = "Endpoints para la gestión del ciclo de vida de las órdenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @Operation(summary = "Listar todas las órdenes", description = "Obtiene el historial completo de órdenes registradas.") 
    @GetMapping
    public ResponseEntity<List<Orden>> listar() {
        return ResponseEntity.ok(ordenService.buscarTodas());
    }

    @Operation(summary = "Obtener orden por ID", description = "Consulta los detalles y el estado actual de una orden específica.") 
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden encontrada"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", 
                     content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtener(@Parameter(description = "UUID de la orden") @PathVariable UUID id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @Operation(summary = "Crear nueva orden", description = "Registra una orden en el sistema y retorna su ubicación.") 
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Orden creada exitosamente",
            headers = @Header(name = "Location", description = "URI del recurso creado", schema = @Schema(type = "string", format = "uri")) 
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", 
                     content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<Orden> crear(@Valid @RequestBody CrearOrdenRequest request) { 
        Orden orden = ordenService.crear(request);
        
        // Generación del header Location según Práctica 4 
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orden.getId().getId())
                .toUri();
        
        return ResponseEntity.created(location).body(orden);
    }

    @Operation(summary = "Cancelar una orden", description = "Cancela una orden pendiente si cumple con las reglas de negocio.") 
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Orden cancelada exitosamente"),
        @ApiResponse(responseCode = "422", description = "No se puede cancelar en el estado actual", 
                     content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(
            @PathVariable UUID id, 
            @Parameter(description = "Motivo de la cancelación (min. 10 caracteres)") 
            @RequestParam(defaultValue = "Cancelado por usuario") String motivo) {
        ordenService.cancelar(id, motivo);
        return ResponseEntity.noContent().build();
    }
}