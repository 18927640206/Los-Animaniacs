package com.uamishop.catalogo.domain;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductoId implements java.io.Serializable {
    private String id;
    
    public ProductoId() {} // Constructor vacío

    public ProductoId(String id) { this.id = id; }
    public String getId() { return id; }
}
