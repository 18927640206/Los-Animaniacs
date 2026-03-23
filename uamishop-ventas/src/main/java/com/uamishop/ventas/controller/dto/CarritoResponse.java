package com.uamishop.ventas.controller.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CarritoResponse {

    private UUID id;
    private UUID clienteId;
    private List<AgregarItemResponse> items;
    private BigDecimal total;

    // getters y setters
}