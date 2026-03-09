package com.uamishop.catalogo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import com.uamishop.catalogo.domain.ProductoEstadisticas;
import com.uamishop.catalogo.repository.ProductoEstadisticasJpaRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;


@Service
public class ProductoEstadisticasService {

    private final ProductoEstadisticasJpaRepository repository;

    public ProductoEstadisticasService(ProductoEstadisticasJpaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public void registrarVenta(UUID productoId, int cantidad) {

        ProductoEstadisticas stats = repository.findById(productoId)
                .orElseGet(() -> new ProductoEstadisticas(productoId));

        stats.setVentasTotales(stats.getVentasTotales() + 1);
        stats.setCantidadVendida(stats.getCantidadVendida() + cantidad);
        stats.setUltimaVentaAt(Instant.now());

        repository.saveAndFlush(stats);
    }

    @Transactional
    public void registrarAgregadoAlCarrito(UUID productoId) {

        ProductoEstadisticas stats = repository.findById(productoId)
                .orElseGet(() -> new ProductoEstadisticas(productoId));

        stats.setVecesAgregadoAlCarrito(stats.getVecesAgregadoAlCarrito() + 1);
        stats.setUltimaAgregadoAlCarritoAt(Instant.now());

        repository.saveAndFlush(stats);
    }

    @Transactional
    public List<ProductoEstadisticas> obtenerMasVendidos(int limit) {

        
        return repository.findMasVendidos(limit);
    }
    
    @Transactional(readOnly = true)
    public ProductoEstadisticas obtenerEstadisticas(UUID productoId) {

        return repository.findById(productoId)
                .orElse(null);
    }
}