package com.uamishop.ventas.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AgregarItemRequest {

    /*LAS VALIDACIONES SE ACTIVAN EN EL CONTROLLER */

    @NotBlank(message = "El productoId es obligatorio")
    private String productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad; //Se coloca en Integer porque si es int nunca puede ser null

    @NotNull(message = "El precioUnitario es obligatorio")
    @Positive(message = "El precioUnitario debe ser mayor a 0")
    private BigDecimal precioUnitario;

    public String getProductoId() { 
        return productoId; 
    }

    public void setProductoId(String productoId) { 
        this.productoId = productoId; 
    }

    public Integer getCantidad() { 
        return cantidad; 
    }

    public void setCantidad(Integer cantidad) { 
        this.cantidad = cantidad; 
    }

    public BigDecimal getPrecioUnitario() { 
        return precioUnitario; 
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) { 
        this.precioUnitario = precioUnitario; 
    }
}