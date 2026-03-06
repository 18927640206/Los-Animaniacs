package com.uamishop.catalogo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "producto_estadisticas")
public class ProductoEstadisticas {

    @Id
    @Column(columnDefinition = "VARBINARY(16)")
    private UUID productoId;

    private long ventasTotales;              // número de transacciones
    private long cantidadVendida;            // unidades vendidas
    private long vecesAgregadoAlCarrito;     // número de veces agregado al carrito

    private Instant ultimaVentaAt;
    private Instant ultimaAgregadoAlCarritoAt;

    // Constructor vacío (requerido por JPA)
    public ProductoEstadisticas() {
    }

    // Constructor con productoId
    public ProductoEstadisticas(UUID productoId) {
        this.productoId = productoId;
        this.ventasTotales = 0;
        this.cantidadVendida = 0;
        this.vecesAgregadoAlCarrito = 0;
    }

    // Getters y Setters

    public UUID getProductoId() {
        return productoId;
    }

    public void setProductoId(UUID productoId) {
        this.productoId = productoId;
    }

    public long getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(long ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public long getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(long cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public long getVecesAgregadoAlCarrito() {
        return vecesAgregadoAlCarrito;
    }

    public void setVecesAgregadoAlCarrito(long vecesAgregadoAlCarrito) {
        this.vecesAgregadoAlCarrito = vecesAgregadoAlCarrito;
    }

    public Instant getUltimaVentaAt() {
        return ultimaVentaAt;
    }

    public void setUltimaVentaAt(Instant ultimaVentaAt) {
        this.ultimaVentaAt = ultimaVentaAt;
    }

    public Instant getUltimaAgregadoAlCarritoAt() {
        return ultimaAgregadoAlCarritoAt;
    }

    public void setUltimaAgregadoAlCarritoAt(Instant ultimaAgregadoAlCarritoAt) {
        this.ultimaAgregadoAlCarritoAt = ultimaAgregadoAlCarritoAt;
    }
}