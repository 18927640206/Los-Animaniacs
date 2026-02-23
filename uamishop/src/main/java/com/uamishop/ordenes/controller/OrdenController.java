package com.uamishop.ordenes.controller;

import com.uamishop.ordenes.controller.dto.OrdenRequest;
import com.uamishop.ordenes.controller.dto.OrdenResponse;
import com.uamishop.ordenes.controller.dto.EstadoRequest;
import com.uamishop.ordenes.service.OrdenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @GetMapping
    public ResponseEntity<List<OrdenResponse>> listar() {
        return ResponseEntity.ok(ordenService.listarPorUsuario());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrdenResponse> crear(@Valid @RequestBody OrdenRequest request) {
        OrdenResponse response = ordenService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenResponse> actualizarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody EstadoRequest request) {

        return ResponseEntity.ok(ordenService.actualizarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        ordenService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
