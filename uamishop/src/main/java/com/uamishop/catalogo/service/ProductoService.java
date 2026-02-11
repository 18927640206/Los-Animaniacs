package com.uamishop.catalogo.service;

import com.uamishop.catalogo.domain.*;
import com.uamishop.catalogo.repository.ProductoJpaRepository;
import com.uamishop.shared.exception.DomainException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductoService {

    private final ProductoJpaRepository productoRepository;

    public ProductoService(ProductoJpaRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crearProducto(
            String nombre,
            String descripcion,
            BigDecimal precio,
            Categoria categoria
    ) {
        Money money = new Money(precio);

        Producto producto = new Producto(
                new ProductoId(),
                nombre,
                descripcion,
                money,
                categoria
        );

        return productoRepository.save(producto);
    }

    public Producto cambiarPrecio(UUID productoId, BigDecimal nuevoPrecio) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new DomainException("Producto no encontrado"));

        producto.cambiarPrecio(new Money(nuevoPrecio));

        return productoRepository.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProducto(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new DomainException("Producto no encontrado"));
    }
}
