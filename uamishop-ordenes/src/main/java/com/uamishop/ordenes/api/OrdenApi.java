// uamishop-ordenes/src/main/java/com/uamishop/ordenes/api/OrdenApi.java
// umaishop-ordenes/src/main/java/com/uamishop/ordenes/api/OrdenApi.java
package com.uamishop.ordenes.api;

import java.util.UUID;

public interface OrdenApi {
    OrdenResumen obtenerResumen(UUID id);
    void cancelar(UUID id, String motivo);
}