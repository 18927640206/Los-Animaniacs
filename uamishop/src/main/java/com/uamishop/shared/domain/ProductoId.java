/*package com.uamishop.catalogo.domain;
import jakarta.persistence.Embeddable;*/

package com.uamishop.shared.domain; 
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductoId implements java.io.Serializable {
    private String id;
    
    public ProductoId() {} // Constructor vacío

    public ProductoId(String id) { this.id = id; }
    public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoId that = (ProductoId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
