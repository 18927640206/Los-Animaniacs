package com.uamishop.ordenes.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CrearOrdenRequest {

    @NotBlank(message = "El clienteId es obligatorio")
    private String clienteId;

    @Valid
    @NotEmpty(message = "La orden debe tener al menos un item")
    private List<ItemOrdenRequest> items;

    @Valid
    private DireccionEnvioRequest direccionEnvio;

    // Getters y Setters
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public List<ItemOrdenRequest> getItems() { return items; }
    public void setItems(List<ItemOrdenRequest> items) { this.items = items; }

    public DireccionEnvioRequest getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(DireccionEnvioRequest direccionEnvio) { this.direccionEnvio = direccionEnvio; }
}