package com.uamishop.catalogo.domain;

import com.uamishop.catalogo.shared.domain.*;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Categoria {
    
    @EmbeddedId
    private CategoriaId id;
    
    private String nombre;
    private String descripcion;

    @ManyToOne
    private Categoria categoriaPadre;

    protected Categoria() {} // Constructor vacío exigido por JPA

    public Categoria(CategoriaId id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void asignarCategoriaPadre(Categoria padre) {
        this.categoriaPadre = padre;
    }
    
    public void actualizarNombre(String nombre) { this.nombre = nombre; }
    public void actualizarDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void asignarPadre(Categoria padre) { this.categoriaPadre = padre; }
    
    // Getters
    public CategoriaId getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Categoria getCategoriaPadre() { return categoriaPadre; }
}