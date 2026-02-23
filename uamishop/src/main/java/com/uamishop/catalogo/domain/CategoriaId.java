package com.uamishop.catalogo.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoriaId implements Serializable {
    private String id;

    protected CategoriaId() {} // Constructor vacío exigido por JPA

    public CategoriaId(String id) { this.id = id; }
    public String getId() { return id; }
}