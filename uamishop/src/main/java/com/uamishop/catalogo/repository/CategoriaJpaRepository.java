package com.uamishop.catalogo.repository;
import com.uamishop.catalogo.domain.Categoria;
import com.uamishop.catalogo.domain.CategoriaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaJpaRepository extends JpaRepository<Categoria, CategoriaId> {
    
}