package com.uamishop.ordenes.api;

import java.util.UUID;

public interface OrdenApi {
    OrdenResumen obtenerResumen(UUID id);
    void cancelar(UUID id, String motivo);
}