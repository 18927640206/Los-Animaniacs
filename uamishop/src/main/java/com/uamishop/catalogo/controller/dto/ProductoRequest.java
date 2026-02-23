package com.uamishop.catalogo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Datos de entrada para crear o actualizar un producto")
public class ProductoRequest {

    @Schema(description = "Nombre del producto", example = "Camiseta negra", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Camiseta 100% algodón, talla M", maxLength = 300)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no puede exceder 300 caracteres")
    private String descripcion;

    @Schema(description = "Precio del producto (mayor a 0)", example = "29.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Schema(description = "Identificador de la categoría a la que pertenece el producto", example = "cat-123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La categoriaId es obligatoria")
    private String categoriaId;

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }
}
