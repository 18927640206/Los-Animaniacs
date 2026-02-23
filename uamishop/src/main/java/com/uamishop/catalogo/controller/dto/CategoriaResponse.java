package com.uamishop.catalogo.controller.dto;

import java.util.UUID;

public class CategoriaResponse {

    private UUID id;
    private String nombre;
    private String descripcion;

    // Constructores
    public CategoriaResponse() {}

    public CategoriaResponse(UUID id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters reales (necesarios para el JSON)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}