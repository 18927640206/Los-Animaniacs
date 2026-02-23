package com.uamishop.catalogo.repository;

import com.uamishop.catalogo.domain.Producto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductoJpaRepository {
    // Métodos falsos para que el servicio no marque error al compilar
    public Producto save(Producto producto) { return producto; }
    public Optional<Producto> findById(UUID id) { return Optional.empty(); }
    public List<Producto> findAll() { return new ArrayList<>(); }
    public void deleteById(UUID id) {}
}