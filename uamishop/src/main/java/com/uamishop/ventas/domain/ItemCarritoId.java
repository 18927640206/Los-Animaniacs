package com.uamishop.ventas.domain;

import jakarta.persistence.Embeddable; // 1. Importa esto
import java.io.Serializable;

@Embeddable // 2. Añade esta anotación
public class ItemCarritoId implements Serializable { // 3. Implementa Serializable
    private String id;

    // 4. JPA necesita un constructor vacío
    protected ItemCarritoId() {}

    public ItemCarritoId(String id) { 
        this.id = id; 
    }
    
    public String getId() { 
        return id; 
    }
    
    // 5. Es buena práctica implementar equals y hashCode
    @Override
    public boolean equals(Object o) { ... }                
    @Override
    public int hashCode() { ... }
}


/*package com.uamishop.ventas.domain;

public class ItemCarritoId {
    private final String id;
    public ItemCarritoId(String id) { this.id = id; }
    public String getId() { return id; }
	

}*/
