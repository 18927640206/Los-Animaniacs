package com.uamishop.ventas.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CarritoId implements Serializable {
    private String id;
    
    protected CarritoId() {} // Obligatorio para JPA
    
    public CarritoId(String id) { this.id = id; }
    public String getId() { return id; }
}
/*package com.uamishop.ventas.domain;

public class CarritoId {
	private final String id;
    public CarritoId(String id) { this.id = id; }
    public String getId() { return id; }
	

}*/
