package com.uamishop.ventas.api;

import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;

public record ItemCarritoResumen(
    ProductoId productoId,
    int cantidad,
    Money precioUnitario
) {}