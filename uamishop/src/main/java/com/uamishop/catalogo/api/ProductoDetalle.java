package com.uamishop.catalogo.api;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoDetalle(
    UUID id,
    String nombre,
    BigDecimal precio,
    boolean isDisponible
) {}