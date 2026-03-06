package com.uamishop.catalogo.controller.dto;

import java.time.Instant;

public record ProductoEstadisticasResponse(
    long ventasTotales,
    long cantidadVendida,
    long vecesAgregadoAlCarrito,
    Instant ultimaVentaAt
) {}