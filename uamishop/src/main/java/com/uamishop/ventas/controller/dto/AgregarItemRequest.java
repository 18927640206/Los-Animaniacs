package com.uamishop.ventas.controller.dto;

import java.math.BigDecimal;

public class AgregarItemRequest {
    // Datos necesarios para agregar un producto al carrito
    private String productoId; // ID único del producto elegido
    private int cantidad; // Cantidad de unidades del producto
    private BigDecimal precioUnitario; // Precio unitario del producto en el carrito

    // Getters y Setters
    
    // Obtiene el ID del producto
    public String getProductoId() { 
        return productoId; 
    }

    // Establece el ID del producto
    public void setProductoId(String productoId) { 
        this.productoId = productoId; 
    }

    // Obtiene la cantidad de productos
    public int getCantidad() { 
        return cantidad; 
    }

    // Establece la cantidad de productos
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
    }

    // Obtiene el precio unitario del producto
    public BigDecimal getPrecioUnitario() { 
        return precioUnitario; 
    }

    // Establece el precio unitario del producto
    public void setPrecioUnitario(BigDecimal precioUnitario) { 
        this.precioUnitario = precioUnitario; 
    }
}