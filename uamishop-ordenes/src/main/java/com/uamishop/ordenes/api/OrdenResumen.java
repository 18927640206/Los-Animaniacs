// uamishop-ordenes/src/main/java/com/uamishop/ordenes/api/OrdenResumen.java
// umaishop-ordenes/src/main/java/com/uamishop/ordenes/api/OrdenResumen.java
package com.uamishop.ordenes.api;

import java.math.BigDecimal;
import java.util.UUID;

public record OrdenResumen(
    UUID id,
    String estado,
    BigDecimal total
) {}