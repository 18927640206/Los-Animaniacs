package com.uamishop.ventas.domain;

import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ClienteId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Carrito {
    private final CarritoId id;
    private final ClienteId clienteId;
    private EstadoCarrito estado;
    private final List<ItemCarrito> items;
    private DescuentoAplicado descuentoAplicado;

    public Carrito(CarritoId id, ClienteId clienteId) {
        this.id = id;
        this.clienteId = clienteId;
        this.estado = EstadoCarrito.ACTIVO;
        this.items = new ArrayList<>();
        this.descuentoAplicado = null;
    }

    public void agregarProducto(ProductoRef productoRef, int cantidad, Money precio) {
        validarModificable();
        
        // RN-VEN-03
        if (items.size() >= 20 && !existeProducto(productoRef.getProductoId())) {
            throw new IllegalStateException("Máximo 20 productos diferentes");
        }
        
        Optional<ItemCarrito> itemExistente = buscarItem(productoRef.getProductoId());
        
        if (itemExistente.isPresent()) {
            // RN-VEN-04
            ItemCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            if (nuevaCantidad > 10) {
                throw new IllegalArgumentException("Máximo 10 unidades por producto");
            }
            items.remove(item);
            items.add(new ItemCarrito(item.getId(), productoRef, nuevaCantidad, precio));
        } else {
            items.add(new ItemCarrito(
                new ItemCarritoId("ITEM-" + items.size()),
                productoRef, cantidad, precio
            ));
        }
    }

    public void modificarCantidad(com.uamishop.catalogo.domain.ProductoId productoId, int nuevaCantidad) {
        validarModificable();
        // RN-VEN-05
        if (nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La nueva cantidad debe ser > 0. Use eliminarProducto.");
        }
        
        ItemCarrito item = buscarItem(productoId)
            .orElseThrow(() -> new IllegalArgumentException("El producto no existe en el carrito"));
            
        if (nuevaCantidad > 10) {
            throw new IllegalArgumentException("Máximo 10 unidades por producto");
        }
        
        items.remove(item);
        items.add(new ItemCarrito(item.getId(), item.getProductoRef(), nuevaCantidad, item.getPrecioUnitario()));
    }

    public void eliminarProducto(com.uamishop.catalogo.domain.ProductoId productoId) {
        validarModificable(); // RN-VEN-07
        ItemCarrito item = buscarItem(productoId)
            .orElseThrow(() -> new IllegalArgumentException("El producto no existe en el carrito")); // RN-VEN-08
        items.remove(item);
    }

    public void vaciar() {
        validarModificable(); // RN-VEN-09
        items.clear();
        descuentoAplicado = null;
    }

    public void iniciarCheckout() {
        // RN-VEN-10
        if (items.isEmpty()) {
            throw new IllegalStateException("Carrito vacío");
        }
        
        // RN-VEN-11
        if (estado != EstadoCarrito.ACTIVO) {
            throw new IllegalStateException("Carrito no está ACTIVO");
        }
        
        // RN-VEN-12
        if (calcularSubtotal().getMonto().compareTo(new BigDecimal("50")) <= 0) {
            throw new IllegalStateException("Mínimo $50 de compra");
        }
        
        estado = EstadoCarrito.EN_CHECKOUT;
    }

    public void completarCheckout() {
        // RN-VEN-13
        if (estado != EstadoCarrito.EN_CHECKOUT) {
            throw new IllegalStateException("Solo se puede completar si está EN_CHECKOUT");
        }
        estado = EstadoCarrito.COMPLETADO;
    }

    public void abandonar() {
        // RN-VEN-14
        if (estado != EstadoCarrito.EN_CHECKOUT) {
            throw new IllegalStateException("Solo se puede abandonar si está EN_CHECKOUT");
        }
        estado = EstadoCarrito.ABANDONADO;
    }

    public void aplicarDescuento(String codigo, BigDecimal porcentaje) {
        // RN-VEN-15
        if (descuentoAplicado != null) {
            throw new IllegalStateException("Ya tiene un descuento");
        }
        
        descuentoAplicado = new DescuentoAplicado(codigo, porcentaje, calcularSubtotal());
    }

    private void validarModificable() {
        if (estado == EstadoCarrito.EN_CHECKOUT) {
            throw new IllegalStateException("No se puede modificar en checkout");
        }
    }

    private boolean existeProducto(com.uamishop.catalogo.domain.ProductoId productoId) {
        return items.stream()
            .anyMatch(item -> item.getProductoRef().getProductoId().equals(productoId));
    }

    private Optional<ItemCarrito> buscarItem(com.uamishop.catalogo.domain.ProductoId productoId) {
        return items.stream()
            .filter(item -> item.getProductoRef().getProductoId().equals(productoId))
            .findFirst();
    }

    public Money calcularSubtotal() {
        return items.stream()
            .map(ItemCarrito::calcularSubtotal)
            .reduce(new Money(new BigDecimal("0"), "MXN"), Money::sumar);
    }
    
    // Getters
    public CarritoId getId() { return id; }
    public EstadoCarrito getEstado() { return estado; }
    public List<ItemCarrito> getItems() { return new ArrayList<>(items); }
    public DescuentoAplicado getDescuentoAplicado() { return descuentoAplicado; }
}