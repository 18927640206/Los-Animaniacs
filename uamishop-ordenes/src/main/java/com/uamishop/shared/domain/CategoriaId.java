/*package com.uamishop.catalogo.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
*/

package com.uamishop.shared.domain; 

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CategoriaId implements Serializable {
    private String id;

    protected CategoriaId() {} // Constructor vacío exigido por JPA

    public CategoriaId(String id) { this.id = id; }
    public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaId that = (CategoriaId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}