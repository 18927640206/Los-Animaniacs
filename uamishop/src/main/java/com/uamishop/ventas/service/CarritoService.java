package com.uamishop.ventas.service;

import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.domain.CarritoId;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import com.uamishop.catalogo.domain.ProductoId;
import com.uamishop.ventas.repository.CarritoJpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarritoService {

    private final CarritoJpaRepository carritoRepository;

    public CarritoService(CarritoJpaRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public Carrito crearCarrito(ClienteId clienteId) {
        Carrito carrito = new Carrito(
            new CarritoId(UUID.randomUUID().toString()),
            clienteId);
        return carritoRepository.save(carrito);
    }

    public Carrito obtenerCarrito(UUID carritoId) {
        return carritoRepository.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    public Carrito agregarItem(UUID carritoId, ProductoRef producto, int cantidad, Money precioUnitario) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.agregarProducto(producto, cantidad, precioUnitario);
        return carritoRepository.save(carrito);
    }

    public Carrito actualizarCantidad(UUID carritoId, String productoIdStr, int nuevaCantidad) {
        Carrito carrito = obtenerCarrito(carritoId);
        ProductoId productoId = new ProductoId(productoIdStr);
        carrito.modificarCantidad(productoId, nuevaCantidad);
        return carritoRepository.save(carrito);
    }

    public Carrito eliminarItem(UUID carritoId, String productoIdStr) {
        Carrito carrito = obtenerCarrito(carritoId);
        ProductoId productoId = new ProductoId(productoIdStr);
        carrito.eliminarProducto(productoId);
        return carritoRepository.save(carrito);
    }

    public Carrito vaciar(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.vaciar();
        return carritoRepository.save(carrito);
    }

    public Carrito iniciarCheckout(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.iniciarCheckout();
        return carritoRepository.save(carrito);
    }

    public Carrito completarCheckout(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.completarCheckout();
        return carritoRepository.save(carrito);
    }

    public Carrito abandonar(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.abandonar();
        return carritoRepository.save(carrito);
    }
}