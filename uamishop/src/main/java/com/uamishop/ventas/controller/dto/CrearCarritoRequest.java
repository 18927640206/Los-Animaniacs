package com.uamishop.ventas.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class CrearCarritoRequest {

    //Usamos la validación de String no nulo y con al menos un caracter visible
    @NotBlank(message = "El clienteId es obligatorio")
    private String clienteId;

    public String getClienteId() { 
        return clienteId; 
    }

    public void setClienteId(String clienteId) { 
        this.clienteId = clienteId; 
    }
}