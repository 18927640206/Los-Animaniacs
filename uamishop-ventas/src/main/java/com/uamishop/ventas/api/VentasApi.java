package com.uamishop.ventas.api;

import java.util.UUID;

public interface VentasApi {
    CarritoResumen obtenerResumen(UUID CarritoId);
    void completarCheckout(UUID carritoId);
}