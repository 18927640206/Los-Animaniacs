package com.uamishop.ordenes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "DTO para la creación de una nueva orden de compra")
public class CrearOrdenRequest {

    @Schema(description = "Identificador único del cliente", example = "USR-123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El clienteId es obligatorio")
    private String clienteId;

    @Schema(description = "Lista de productos incluidos en la orden")
    @Valid // Valida recursivamente cada item en la lista [cite: 40]
    @NotEmpty(message = "La orden debe tener al menos un item") // RN-ORD-01 [cite: 623]
    private List<ItemOrdenRequest> items;

    @Schema(description = "Información detallada para el envío")
    @Valid // Valida el objeto de dirección [cite: 40]
    @NotNull(message = "La dirección de envío es obligatoria")
    private DireccionEnvioRequest direccionEnvio;

    // Getters y Setters
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public List<ItemOrdenRequest> getItems() { return items; }
    public void setItems(List<ItemOrdenRequest> items) { this.items = items; }

    public DireccionEnvioRequest getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(DireccionEnvioRequest direccionEnvio) { this.direccionEnvio = direccionEnvio; }
}