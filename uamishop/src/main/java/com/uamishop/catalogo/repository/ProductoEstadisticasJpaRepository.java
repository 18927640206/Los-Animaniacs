package com.uamishop.catalogo.repository;

import com.uamishop.catalogo.domain.ProductoEstadisticas;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad ProductoEstadisticas.
 * Permite realizar operaciones CRUD sobre las estadísticas de productos.
 */
public interface ProductoEstadisticasJpaRepository extends JpaRepository<ProductoEstadisticas, UUID> {

    /**
     * Obtiene los productos más vendidos ordenados por la cantidad vendida
     * en orden descendente.
     *
     * @param limit número máximo de productos a devolver
     * @return lista de productos con mayores ventas
     */
    default List<ProductoEstadisticas> findMasVendidos(int limit) {
        return findAll(Sort.by(Sort.Direction.DESC, "cantidadVendida"))
                .stream()
                .limit(limit)
                .toList();
    }
}