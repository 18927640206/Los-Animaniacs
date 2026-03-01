package com.uamishop.catalogo.api;

import java.util.UUID;
import java.util.Optional;

public interface CatalogoApi {
    Optional<ProductoDetalle> obtenerDetalleProducto(UUID productoId);
    boolean hayStockDisponible(UUID productoId, int cantidad);
}