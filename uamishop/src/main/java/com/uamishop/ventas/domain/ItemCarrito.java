package com.uamishop.ventas.domain;

import com.uamishop.shared.domain.Money;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverride; 
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Embeddable
public class ItemCarrito {
    
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "item_id"))
    private ItemCarritoId id;

    @Embedded
    private ProductoRef productoRef;

    private int cantidad;

    @Embedded // JPA necesita que no sea final para que pueda llenarse
    private  Money precioUnitario;

    protected ItemCarrito() {}

    public ItemCarrito(ItemCarritoId id, ProductoRef productoRef, int cantidad, Money precioUnitario) {
        // RN-VEN-01
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser > 0");
        }
        
        // RN-VEN-02
        if (cantidad > 10) {
            throw new IllegalArgumentException("Máximo 10 unidades");
        }
        
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
    
    // Getters
    public ItemCarritoId getId() { return id; }
    public ProductoRef getProductoRef() { return productoRef; }
    public int getCantidad() { return cantidad; }
    public Money getPrecioUnitario() { return precioUnitario; }
}
