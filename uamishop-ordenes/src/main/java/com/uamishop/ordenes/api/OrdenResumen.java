package com.uamishop.ordenes.api;

import java.math.BigDecimal;
import java.util.UUID;

public record OrdenResumen(
    UUID id,
    String estado,
    BigDecimal total
) {}