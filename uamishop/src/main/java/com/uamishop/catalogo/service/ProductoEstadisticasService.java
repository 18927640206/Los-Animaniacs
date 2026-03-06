package com.uamishop.catalogo.service;

//import com.uamishop.catalogo.domain.CategoriaId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import com.uamishop.catalogo.domain.ProductoEstadisticas;
import com.uamishop.catalogo.repository.ProductoEstadisticasJpaRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

/**
 * Registra una venta de un producto.
 * Si el producto no tiene estadísticas previas, se crean.
 * Incrementa el número de ventas totales y la cantidad vendida.
 */
@Service
@RequiredArgsConstructor
public class ProductoEstadisticasService {

    private final ProductoEstadisticasJpaRepository repository;

    /**
     * Registra que un producto fue agregado al carrito.
     * Incrementa el contador de veces agregado al carrito
     * y actualiza la fecha del último agregado.
     */
    public void registrarVenta(UUID productoId, int cantidad) {

        ProductoEstadisticas stats = repository.findById(productoId)
                .orElse(new ProductoEstadisticas(productoId));

        stats.setVentasTotales(stats.getVentasTotales() + 1);
        stats.setCantidadVendida(stats.getCantidadVendida() + cantidad);
        stats.setUltimaVentaAt(Instant.now());

        repository.save(stats);
    }

    /**
     * Registra que un producto fue agregado al carrito.
     * Incrementa el contador de veces agregado al carrito
     * y actualiza la fecha del último agregado.
     */
    public void registrarAgregadoAlCarrito(UUID productoId) {

        ProductoEstadisticas stats = repository.findById(productoId)
                .orElse(new ProductoEstadisticas(productoId));

        stats.setVecesAgregadoAlCarrito(stats.getVecesAgregadoAlCarrito() + 1);
        stats.setUltimaAgregadoAlCarritoAt(Instant.now());

        repository.save(stats);
    }

    // Obtener productos más vendidos
    public List<ProductoEstadisticas> obtenerMasVendidos(int limit) {

        // versión simple (puedes mejorar con Pageable)
        return repository.findMasVendidos(limit);
    }

    // Obtener estadísticas de un producto
    public ProductoEstadisticas obtenerEstadisticas(UUID productoId) {

        return repository.findById(productoId)
                .orElse(null);
    }
}