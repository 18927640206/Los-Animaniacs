package com.uamishop.catalogo.shared.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Evento que representa la compra de productos. 
 * Se define en Shared porque lo emite Órdenes y lo escuchará Catálogo.
 */

public record ProductoCompradoEvent(
    UUID eventId,
    Instant occurredAt,
    UUID ordenId,
    UUID clienteId,
    List<ItemComprado> items
) {
    public record ItemComprado(
        UUID productoId,
        String sku,
        int cantidad,
        BigDecimal precioUnitario,
        String moneda
    ) {}
}