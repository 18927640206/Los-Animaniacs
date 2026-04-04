// uamishop-ordenes/src/main/java/com/uamishop/shared/event/ProductoAgregadoAlCarritoEvent.java
package com.uamishop.shared.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento que representa que un usuario añadió un producto al carrito.
 */

public record ProductoAgregadoAlCarritoEvent(
    UUID eventId,
    Instant occurredAt,
    UUID productoId,
    UUID carritoId,
    int cantidad,
    BigDecimal precioUnitario,
    String moneda
) {}