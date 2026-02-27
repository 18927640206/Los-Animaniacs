package com.uamishop.catalogo.service;

import com.uamishop.catalogo.controller.dto.ProductoRequest;
import com.uamishop.catalogo.controller.dto.ProductoResponse;
import com.uamishop.catalogo.domain.*;
import com.uamishop.catalogo.repository.ProductoJpaRepository;
import com.uamishop.catalogo.repository.CategoriaJpaRepository;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.exception.DomainException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoJpaRepository productoRepository;
    private final CategoriaJpaRepository categoriaRepository;

    public ProductoService(ProductoJpaRepository productoRepository, CategoriaJpaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProductoResponse crear(ProductoRequest request) {
        // Convertimos el String del Request al Value Object CategoriaId
        CategoriaId catId = new CategoriaId(request.getCategoriaId());
        
        Producto producto = new Producto(
            new ProductoId(UUID.randomUUID().toString()),
            request.getNombre(),
            request.getDescripcion(),
            new Money(request.getPrecio(), "MXN"),
            catId
        );
        
        productoRepository.save(producto);
        return mapearAResponse(producto);
    }

    public ProductoResponse actualizar(UUID id, ProductoRequest request) {
        // Buscamos usando el Value Object ProductoId en lugar del UUID directamente
        Producto producto = productoRepository.findById(new ProductoId(id.toString()))
            .orElseThrow(() -> new DomainException("Producto no encontrado"));

        if (request.getPrecio() != null) {
            producto.cambiarPrecio(new Money(request.getPrecio(), "MXN"));
        }
        
        productoRepository.save(producto);
        return mapearAResponse(producto);
    }

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public ProductoResponse buscarPorId(UUID id) {
        Producto producto = productoRepository.findById(new ProductoId(id.toString()))
            .orElseThrow(() -> new DomainException("Producto no encontrado"));
        return mapearAResponse(producto);
    }

    public void activar(UUID id) {
        Producto producto = productoRepository.findById(new ProductoId(id.toString()))
                .orElseThrow(() -> new DomainException("Producto no encontrado"));
        producto.activar();
        productoRepository.save(producto);
    }

    public void desactivar(UUID id) {
        Producto producto = productoRepository.findById(new ProductoId(id.toString()))
                .orElseThrow(() -> new DomainException("Producto no encontrado"));
        producto.desactivar();
        productoRepository.save(producto);
    }

    public void eliminar(UUID id) {
        productoRepository.deleteById(new ProductoId(id.toString()));
    }

    // He simplificado el mapeo para obtener el UUID real de los Value Objects
    private ProductoResponse mapearAResponse(Producto producto) {
        return new ProductoResponse(
            UUID.fromString(producto.getId().getId()),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio().getMonto(),
            UUID.fromString(producto.getCategoriaId().getId()),
            producto.isDisponible() ? "ACTIVO" : "INACTIVO"
        );
    }
}