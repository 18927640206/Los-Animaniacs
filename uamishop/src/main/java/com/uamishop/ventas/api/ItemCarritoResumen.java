package com.uamishop.ventas.api;

import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;

public record ItemCarritoResumen(
    ProductoId productoId,
    String nombreProducto, // Agregado según Práctica 5
    String sku,            // Agregado según Práctica 5
    int cantidad,
    Money precioUnitario
) {}