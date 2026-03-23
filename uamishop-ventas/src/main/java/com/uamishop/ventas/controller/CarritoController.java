// Archivo: /workspaces/Los-Animaniacs/uamishop-ventas/src/main/java/com/uamishop/ventas/controller/CarritoController.java

package com.uamishop.ventas.controller;

import com.uamishop.ventas.api.CarritoResumen;
import com.uamishop.ventas.controller.dto.AgregarItemRequest;
import com.uamishop.ventas.controller.dto.CrearCarritoRequest;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.shared.domain.ProductoRef;
import com.uamishop.shared.domain.ProductoId;
import com.uamishop.ventas.service.CarritoService;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    // Eliminamos la dependencia de OrdenApi para desacoplar los servicios
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping
    public ResponseEntity<Carrito> crear(@Valid @RequestBody CrearCarritoRequest request) {
        ClienteId clienteId = new ClienteId(request.getClienteId());
        Carrito carrito = carritoService.crearCarrito(clienteId);
        return ResponseEntity.created(URI.create("/api/v2/carritos/" + carrito.getId())).body(carrito);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(id));
    }

    // --- REQUERIDO PARA EL PASO 1.1 ---
    // Este endpoint permite que el microservicio de Órdenes obtenga los datos del carrito vía HTTP
    @GetMapping("/{id}/resumen")
    public ResponseEntity<CarritoResumen> obtenerResumen(@PathVariable UUID id) {
        return ResponseEntity.ok(carritoService.obtenerResumen(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Carrito> agregarItem(@PathVariable UUID id, @Valid @RequestBody AgregarItemRequest req) {
        Money precio = new Money(req.getPrecioUnitario(), "MXN");
        ProductoRef ref = new ProductoRef(new ProductoId(req.getProductoId()), "Producto Demo", "SKU-001");
        return ResponseEntity.ok(carritoService.agregarItem(id, ref, req.getCantidad(), precio));
    }

    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<Carrito> vaciar(@PathVariable UUID id) {
        return ResponseEntity.ok(carritoService.vaciar(id));
    }
}