package com.uamishop.ordenes.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class DireccionEnvioRequest {

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El codigo postal es obligatorio")
    private String codigoPostal;

    @NotBlank(message = "El pais es obligatorio")
    private String pais;

    // getters y setters
}