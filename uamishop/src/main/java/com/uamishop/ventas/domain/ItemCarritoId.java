package com.uamishop.ventas.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

import java.util.Objects;

@Embeddable 
public class ItemCarritoId implements Serializable { 
    private String id;

    // JPA necesita un constructor vacío
    protected ItemCarritoId() {}

    public ItemCarritoId(String id) { 
        this.id = id; 
    }
    
    public String getId() { 
        return id; 
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCarritoId that = (ItemCarritoId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


/*package com.uamishop.ventas.domain;

public class ItemCarritoId {
    private final String id;
    public ItemCarritoId(String id) { this.id = id; }
    public String getId() { return id; }
	

}*/
