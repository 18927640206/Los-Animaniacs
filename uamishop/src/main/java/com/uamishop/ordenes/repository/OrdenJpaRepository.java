package com.uamishop.ordenes.repository;

import com.uamishop.ordenes.domain.Orden;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrdenJpaRepository {
    // Métodos falsos para que el servicio no marque error al compilar
    public Orden save(Orden orden) { return orden; }
    public Optional<Orden> findById(UUID id) { return Optional.empty(); }
    public List<Orden> findAll() { return new ArrayList<>(); }
}