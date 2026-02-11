package com.uamishop.catalogo.repository;

import com.uamishop.catalogo.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductoJpaRepository extends JpaRepository<Producto, UUID> {
    //Aqui meteremos lo metodos nuevos si se llegan a requerir.
    //solo declarados pues esta es una interface.
}