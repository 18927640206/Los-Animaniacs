package com.uamishop.ventas.controller;

import com.uamishop.ventas.controller.dto.AgregarItemRequest;
import com.uamishop.ventas.controller.dto.CrearCarritoRequest;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.service.CarritoService;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // Crear carrito
    @PostMapping
    public ResponseEntity<Carrito> crear(@Valid @RequestBody CrearCarritoRequest request) {

        ClienteId clienteId = new ClienteId(request.getClienteId());

        Carrito carrito = carritoService.crear(clienteId);

        return ResponseEntity
                .created(URI.create("/api/carritos/" + carrito.getId()))
                .body(carrito);
    }

    // Obtener carrito
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtener(@PathVariable UUID id) {

        Carrito carrito = carritoService.obtenerCarrito(id);

        return ResponseEntity.ok(carrito);
    }

    // Agregar producto
    @PostMapping("/{id}/items")
    public ResponseEntity<Carrito> agregarItem(
            @PathVariable UUID id,
            @Valid @RequestBody AgregarItemRequest req) {

        Money precio = new Money(req.getPrecioUnitario(), "MXN");

        Carrito carrito = carritoService.agregarProducto(
                id,
                req.toProductoRef(),   // si tienes método helper
                req.getCantidad(),
                precio
        );

        return ResponseEntity.ok(carrito);
    }

    // Iniciar checkout
    @PostMapping("/{id}/checkout")
    public ResponseEntity<Carrito> checkout(@PathVariable UUID id) {

        Carrito carrito = carritoService.iniciarCheckout(id);

        return ResponseEntity.ok(carrito);
    }
}
