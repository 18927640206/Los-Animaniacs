package com.uamishop.ventas.api;

import com.uamishop.shared.domain.ClienteId;
import java.util.List;
import java.util.UUID;

public record CarritoResumen(
    UUID carritoId,
    ClienteId clienteId,
    String estado,
    List<ItemCarritoResumen> items
) {
    public CarritoResumen {
        // Validacion para evitar NullPointerException
        if (items == null) {
            items = List.of();
        }
    }
}