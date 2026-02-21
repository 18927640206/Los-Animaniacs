package com.uamishop.ordenes.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrdenResponse {

    private UUID id;
    private String clienteId;
    private String estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    private DireccionEnvio direccionEnvio;
    private List<ItemOrdenResponse> items;

    // getters y setters
}