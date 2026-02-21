package com.uamishop.catalogo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 200, message = "La descripcion no puede exceder de 200 caracteres")
    private String descripcion;

    // getters y setters
}