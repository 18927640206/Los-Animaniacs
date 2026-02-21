package com.uamishop.ordenes.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemOrdenRequest {

    @NotBlank(message = "El productoId es obligatorio")
    private String productoId;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    private BigDecimal precioUnitario;

    // getters y setters
}