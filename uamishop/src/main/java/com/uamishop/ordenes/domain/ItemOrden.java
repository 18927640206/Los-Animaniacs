package com.uamishop.ordenes.domain;

import com.uamishop.shared.domain.Money;
import com.uamishop.ventas.domain.ProductoRef;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Embeddable
public class ItemOrden {
    
    @Transient
    private ItemOrdenId id;
    
    @Column(name = "item_orden_id")
    private String itemOrdenIdStr;
    
    // CORRECCIÓN: Personalizamos los nombres de las columnas para evitar conflictos con otros módulos
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "productoId.id", column = @Column(name = "ref_producto_id")),
        @AttributeOverride(name = "nombre", column = @Column(name = "ref_producto_nombre")),
        @AttributeOverride(name = "sku", column = @Column(name = "ref_producto_sku"))
    })
    private ProductoRef productoRef;
    
    private int cantidad;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "precio_monto")),
        @AttributeOverride(name = "moneda", column = @Column(name = "precio_moneda"))
    })
    private Money precioUnitario;

    protected ItemOrden() {}

    public ItemOrden(ItemOrdenId id, ProductoRef productoRef, int cantidad, Money precioUnitario) {
        this.id = id;
        this.itemOrdenIdStr = id != null ? id.getId() : null;
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
    
    public ItemOrdenId getId() {
        if (this.id == null && this.itemOrdenIdStr != null) {
            this.id = new ItemOrdenId(this.itemOrdenIdStr);
        }
        return this.id;
    }
}