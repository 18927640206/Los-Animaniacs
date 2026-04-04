// uamishop-ordenes/src/main/java/com/uamishop/ordenes/repository/OrdenJpaRepository.java
package com.uamishop.ordenes.repository;

import com.uamishop.ordenes.domain.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdenJpaRepository extends JpaRepository<Orden, String> {
    
    // Sobrecargamos el método para que acepte UUID y busque por String en la BD.
    // Con esto, Spring Data JPA hace toda la magia sin marcar errores en tu servicio.
    default Optional<Orden> findById(UUID id) {
        return findById(id.toString());
    }
}