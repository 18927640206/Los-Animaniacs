package com.uamishop.ordenes.domain;

import com.uamishop.shared.domain.Money;
import com.uamishop.ventas.domain.ProductoRef;
import java.math.BigDecimal;

public class ItemOrden {
    private final ItemOrdenId id;
    private final ProductoRef productoRef;
    private final int cantidad;
    private final Money precioUnitario;

    public ItemOrden(ItemOrdenId id, ProductoRef productoRef, int cantidad, Money precioUnitario) {
        this.id = id;
        this.productoRef = productoRef;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    public Money calcularSubtotal() {
        return new Money(
            precioUnitario.getMonto().multiply(new BigDecimal(cantidad)),
            precioUnitario.getMoneda()
        );
    }
    
    public ProductoRef getProductoRef() { return productoRef; }
    public int getCantidad() { return cantidad; }
}
