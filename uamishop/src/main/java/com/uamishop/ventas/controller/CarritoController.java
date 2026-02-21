package com.uamishop.ventas.controller;

import com.uamishop.ventas.controller.dto.AgregarItemRequest;
import com.uamishop.ventas.controller.dto.CrearCarritoRequest;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.service.CarritoService;
import com.uamishop.shared.domain.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController // Registra esta clase como un controlador REST en Spring Boot y define la ruta base para las operaciones relacionadas con carritos (Retorna datos en formato JSON y maneja solicitudes HTTP)
@RequestMapping("/api/carritos") // Define la ruta base para las operaciones relacionadas con carritos 
public class CarritoController { // Controlador REST para gestionar carritos de compra y sus operaciones asociadas (crear, obtener, agregar productos, finalizar compra)

    @Autowired
    private CarritoService carritoService;

    // POST /api/carritos - Crea un nuevo carrito para un cliente
    @PostMapping
    public ResponseEntity<Carrito> crear(@Valid @RequestBody CrearCarritoRequest request) { // Maneja la solicitud POST para crear un nuevo carrito, recibe los datos del cliente en el cuerpo de la solicitud y devuelve el carrito creado con su ubicación
        Carrito carrito = carritoService.crearCarrito(UUID.fromString(request.getClienteId())); // Llama al servicio para crear un nuevo carrito utilizando el ID del cliente proporcionado en la solicitud
        return ResponseEntity.created(URI.create("/api/carritos/" + carrito.getId().toString())) // Devuelve una respuesta HTTP 201 Created con la ubicación del nuevo carrito y el carrito creado en el cuerpo de la respuesta
                .body(carrito);// Devuelve el carrito creado en el cuerpo de la respuesta HTTP.
    }

    // GET /api/carritos/{id} - Obtiene un carrito existente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtener(@PathVariable String id) {
        return carritoService.obtenerCarrito(UUID.fromString(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/carritos/{id}/items - Agrega un producto al carrito
    @PostMapping("/{id}/items")
    public ResponseEntity<Carrito> agregarItem(@PathVariable UUID id, @Valid @RequestBody AgregarItemRequest req) {
        // Crea el objeto Money con el precio unitario en moneda MXN
        Money precio = new Money(req.getPrecioUnitario(), "MXN");
        
        // Llama al servicio para agregar el producto al carrito
        Carrito carrito = carritoService.agregarProducto(
                UUID.fromString(id),
                UUID.fromString(req.getProductoId()),
                req.getCantidad(),
                precio
        );
        return ResponseEntity.ok(carrito);
    }

    // POST /api/carritos/{id}/checkout - Finaliza la compra y procesa el carrito
    @PostMapping("/{id}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable String id) {
        // Llama al servicio para completar la compra
        carritoService.finalizarCompra(UUID.fromString(id));
        return ResponseEntity.accepted().build();
    }
}