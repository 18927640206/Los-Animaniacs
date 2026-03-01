package com.uamishop.ventas.service;

import com.uamishop.ventas.api.VentasApi;
import com.uamishop.ventas.api.CarritoResumen;
import com.uamishop.ventas.api.ItemCarritoResumen;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;
//import com.uamishop.catalogo.domain.ProductoId;
import com.uamishop.ventas.repository.CarritoJpaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarritoService implements VentasApi {

    private final CarritoJpaRepository carritoRepository;

    public CarritoService(CarritoJpaRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoResumen obtenerResumen(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        
        var itemsResumen = carrito.getItems().stream()
            .map(item -> new ItemCarritoResumen(
                item.getProductoRef().getProductoId(),
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

    // Convertimos UUID a String en findById

    @Transactional(readOnly = true)
    public Carrito obtenerCarrito(UUID carritoId) {
        return carritoRepository.findById(carritoId.toString()) // .toString() aquí
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    @Transactional
    public Carrito agregarItem(UUID carritoId, ProductoRef producto, int cantidad, Money precioUnitario) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.agregarProducto(producto, cantidad, precioUnitario);
        return carritoRepository.save(carrito);
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
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.completarCheckout();
        carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito abandonar(UUID carritoId) {
        Carrito carrito = obtenerCarrito(carritoId);
        carrito.abandonar();
        return carritoRepository.save(carrito);
    }
}

/*package com.uamishop.ventas.service;

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
}*/