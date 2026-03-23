package com.uamishop.shared.domain;

import jakarta.persistence.Embeddable; // Importante
import java.io.Serializable;

@Embeddable // <--- Agrega esto
public class ClienteId implements Serializable {

    private String id; // Quita el 'final' para que JPA pueda usar el constructor vacío

    protected ClienteId() {} // Constructor vacío obligatorio

    public ClienteId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
/*package com.uamishop.shared.domain;

public class ClienteId {

    private final String id;

    public ClienteId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}*/
