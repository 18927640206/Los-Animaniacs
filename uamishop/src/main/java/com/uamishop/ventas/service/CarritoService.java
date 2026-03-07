package com.uamishop.ventas.service;

import com.uamishop.ventas.api.VentasApi;
import com.uamishop.ventas.api.CarritoResumen;
import com.uamishop.ventas.api.ItemCarritoResumen;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;
import com.uamishop.ventas.repository.CarritoJpaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//para eventos p6
import org.springframework.context.ApplicationEventPublisher;
import java.time.Instant;
import com.uamishop.shared.event.ProductoCompradoEvent;
import com.uamishop.shared.event.ProductoAgregadoAlCarritoEvent;
import com.uamishop.shared.event.OrdenCreadaEvent;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarritoService implements VentasApi {

    private final CarritoJpaRepository carritoRepository;

    //Inyectar el publicador para disparar el evento
    private final ApplicationEventPublisher eventPublisher;

    public CarritoService(CarritoJpaRepository carritoRepository, ApplicationEventPublisher eventPublisher) {
        this.carritoRepository = carritoRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoResumen obtenerResumen(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        
        var itemsResumen = carrito.getItems().stream()
            .map(item -> new ItemCarritoResumen(
                item.getProductoRef().getProductoId(),
                item.getProductoRef().getNombre(), // Extraemos el nombre
                item.getProductoRef().getSku(),    // Extraemos el SKU
                item.getCantidad(),
                item.getPrecioUnitario()
            ))
            .collect(Collectors.toList());

        return new CarritoResumen(
            UUID.fromString(carrito.getId()),
            carrito.getClienteId(),
            carrito.getEstado().toString(),
            itemsResumen
        );
    }

    @Transactional
    public Carrito crearCarrito(ClienteId clienteId) {
        Carrito carrito = new Carrito(
            UUID.randomUUID().toString(),
            clienteId);
        return carritoRepository.save(carrito);
    }

    @Transactional(readOnly = true)
    public Carrito obtenerCarrito(UUID carritoId) {
        return carritoRepository.findById(carritoId.toString())
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    @Transactional
    public Carrito agregarItem(UUID carritoId, ProductoRef producto, int cantidad, Money precioUnitario) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.agregarProducto(producto, cantidad, precioUnitario);
        Carrito carritoGuardado = carritoRepository.save(carrito);

        // Se publica el evento (Paso 1.2 de la práctica 6)
        eventPublisher.publishEvent(new ProductoAgregadoAlCarritoEvent(
            UUID.randomUUID(),
            Instant.now(),
            UUID.fromString(producto.getProductoId().getId()),
            carritoId,
            cantidad,
            precioUnitario.getMonto(),
            precioUnitario.getMoneda()
        ));

        return carritoGuardado;
    }

    @Transactional
    public Carrito actualizarCantidad(UUID carritoId, String productoIdStr, int nuevaCantidad) {
        Carrito carrito = obtenerCarrito(carritoId);
        ProductoId productoId = new ProductoId(productoIdStr);
        carrito.modificarCantidad(productoId, nuevaCantidad);
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarItem(UUID carritoId, String productoIdStr) {
        Carrito carrito = obtenerCarrito(carritoId);
        ProductoId productoId = new ProductoId(productoIdStr);
        carrito.eliminarProducto(productoId);
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito vaciar(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.vaciar();
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito iniciarCheckout(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.iniciarCheckout();
        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void completarCheckout(UUID carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
            .orElseThrow(() -> new CarritoNoEncontradoException(carritoId));

        carrito.COMPLETADO(); //Cambiar a estado COMPLETADO
        carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito abandonar(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.abandonar();
        return carritoRepository.save(carrito);
    }
}