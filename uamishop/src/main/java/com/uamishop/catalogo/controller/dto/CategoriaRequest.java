package com.uamishop.catalogo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID; // Import necesario para el ID del padre

public class CategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio") 
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres") 
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres") 
    private String descripcion;

    private UUID categoriaPadreId; // Campo faltante según el ejemplo de la práctica 

    // Getters y Setters
    public String getNombre() 
    { 
        return nombre; 
        }
    public void setNombre(String nombre) 
    { 
        this.nombre = nombre; 
        }

    public String getDescripcion() {
         return descripcion; 
         }
    public void setDescripcion(String descripcion) 
    { 
        this.descripcion = descripcion; 
        }

    public UUID getCategoriaPadreId()
    { 
        return categoriaPadreId; 
        }
    public void setCategoriaPadreId(UUID categoriaPadreId) 
    { 
        this.categoriaPadreId = categoriaPadreId; 
        }
}