package com.uamishop.ventas.repository;

import com.uamishop.ventas.domain.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarritoJpaRepository extends JpaRepository<Carrito, UUID> {
    
}