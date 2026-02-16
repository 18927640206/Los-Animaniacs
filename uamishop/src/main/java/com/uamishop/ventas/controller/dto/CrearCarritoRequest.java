package com.uamishop.ventas.controller.dto;

public class CrearCarritoRequest {
    // ID del cliente propietario del carrito
    private String clienteId;

    // Getter y Setter

    // Obtiene el ID del cliente de este carrito
    public String getClienteId() { 
        return clienteId; 
    }

    // Establece el ID del cliente dentro del carrito
    public void setClienteId(String clienteId) { 
        this.clienteId = clienteId; 
    }
}