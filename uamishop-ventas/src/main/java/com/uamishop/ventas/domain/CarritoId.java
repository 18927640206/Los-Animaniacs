package com.uamishop.ventas.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class CarritoId implements Serializable {
    private String id;
    
    protected CarritoId() {} 
    
    public CarritoId(String id) { this.id = id; }
    
    // Método necesario para solucionar el error en el Listener 
    public static CarritoId of(UUID uuid) {
        return new CarritoId(uuid.toString());
    }

    public String getId() { return id; }
}