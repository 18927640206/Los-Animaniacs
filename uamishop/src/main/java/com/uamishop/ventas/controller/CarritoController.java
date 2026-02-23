package com.uamishop.ventas.controller;

import com.uamishop.ventas.controller.dto.AgregarItemRequest;
import com.uamishop.ventas.controller.dto.CrearCarritoRequest;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.catalogo.domain.ProductoId;
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

    @PostMapping
    public ResponseEntity<Carrito> crear(@Valid @RequestBody CrearCarritoRequest request) {
        ClienteId clienteId = new ClienteId(request.getClienteId());
        Carrito carrito = carritoService.crearCarrito(clienteId);
        return ResponseEntity.created(URI.create("/api/carritos/" + carrito.getId().getId())).body(carrito);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtener(@PathVariable UUID id) {
        Carrito carrito = carritoService.obtenerCarrito(id);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Carrito> agregarItem(
            @PathVariable UUID id,
            @Valid @RequestBody AgregarItemRequest req) {
        
        Money precio = new Money(req.getPrecioUnitario(), "MXN");
        ProductoRef ref = new ProductoRef(new ProductoId(req.getProductoId()), "Producto Demo", "AAA-000"); // Debería consultar Catálogo idealmente
        
        Carrito carrito = carritoService.agregarItem(id, ref, req.getCantidad(), precio);
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<Carrito> actualizarItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId, // Asumimos que itemId mapea al ProductoId temporalmente por diseño REST
            @RequestParam int cantidad) {
        
        Carrito carrito = carritoService.actualizarCantidad(id, itemId.toString(), cantidad);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Carrito> eliminarItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId) {
            
        Carrito carrito = carritoService.eliminarItem(id, itemId.toString());
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<Carrito> vaciar(@PathVariable UUID id) {
        Carrito carrito = carritoService.vaciar(id);
        return ResponseEntity.ok(carrito);
    }
}