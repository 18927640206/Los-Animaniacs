// uamishop-ordenes/src/main/java/com/uamishop/ordenes/controller/dto/ItemOrdenRequest.java
package com.uamishop.ordenes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Detalle de un producto individual dentro de la orden")
public class ItemOrdenRequest {

    @Schema(description = "ID del producto", example = "PROD-100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El productoId es obligatorio")
    private String productoId;

    @Schema(description = "Cantidad de unidades (mínimo 1)", example = "2")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    @Schema(description = "Precio unitario al momento de la compra", example = "150.00")
    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero") // Regla RN-ORD-02
    private BigDecimal precioUnitario;

    // Getters y Setters
    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}