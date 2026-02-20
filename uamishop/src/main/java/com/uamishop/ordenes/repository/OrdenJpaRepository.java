package com.uamishop.ordenes.repository;

import com.uamishop.ordenes.domain.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, UUID> {
    
}