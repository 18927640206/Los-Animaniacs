package com.uamishop.catalogo.domain;

public class Categoria {
    private final CategoriaId id;
    private String nombre;
    private Categoria categoriaPadre;

    public Categoria(CategoriaId id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void asignarCategoriaPadre(Categoria padre) {
        this.categoriaPadre = padre;
    }
}
