package com.uamishop.ventas.repository;

import com.uamishop.ventas.domain.Carrito;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CarritoJpaRepository {
    // Métodos falsos para que el servicio no marque error al compilar
    public Carrito save(Carrito carrito) { return carrito; }
    public Optional<Carrito> findById(UUID id) { return Optional.empty(); }
}