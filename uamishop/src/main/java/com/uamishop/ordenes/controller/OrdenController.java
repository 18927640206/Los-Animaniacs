package com.uamishop.ordenes.controller;

import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
import com.uamishop.ordenes.domain.Orden;
import com.uamishop.ordenes.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public ResponseEntity<List<Orden>> listar() {
        return ResponseEntity.ok(ordenService.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Orden> crear(@Valid @RequestBody CrearOrdenRequest request) {
        Orden orden = ordenService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id, @RequestParam(defaultValue = "Cancelado por usuario") String motivo) {
        ordenService.cancelar(id, motivo);
        return ResponseEntity.noContent().build();
    }
}