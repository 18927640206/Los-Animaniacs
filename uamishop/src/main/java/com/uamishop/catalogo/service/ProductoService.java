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
    private final CategoriaJpaRepository categoriaRepository;

    public ProductoService(ProductoJpaRepository productoRepository, CategoriaJpaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Producto crearProducto(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
        .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Producto producto = new Producto(
            UUID.randomUUID(),
            request.getNombre(),
            request.getDescripcion(),
            request.getMoney(),
            categoria
        );

        return productoRepository.save(producto);
    }

    public Producto actualizar(UUID id, ProductoRequest request) {

        Producto producto = buscarPorId(id);

        producto.actualizarNombre(request.getNombre());
        producto.actualizarDescripcion(request.getDescripcion());

        if (request.getPrecio() != null) {
            producto.cambiarPrecio(request.getPrecio());
        }

        return productoRepository.save(producto);
    }

    public List<Producto> buscarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(UUID id) {
        return productoRepository.findById(id)
        .orElseThrow(() -> new DomainException("Producto no encontrado"));
    }

    public void activar(UUID id) {
        Producto producto = buscarPorId(id);
        producto.activar();
        productoRepository.save(producto);
    }

    public void desactivar(UUID id) {
        Producto producto = buscarPorId(id);
        producto.desactivar();
        productoRepository.save(producto);
    }

    // Para categoria
    public Categoria crearCategoria(CategoriaRequest request) {

    Categoria categoria = new Categoria(
            new CategoriaId(UUID.randomUUID().toString()),
            request.getNombre()
    );

    if (request.getCategoriaPadreId() != null) {

        Categoria padre = categoriaRepository.findById(
                request.getCategoriaPadreId())
            .orElseThrow(() -> new RuntimeException("Categoria padre no encontrada"));

        categoria.asignarCategoriaPadre(padre);
    }

    return categoriaRepository.save(categoria);
}
    /*public Categoria crearCategoria(CategoriaRequest request) {

        Categoria padre = null;

        if (request.getCategoriaPdreId() != null) {
        padre = Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
        .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        }

        Categoria categoria = new Categoria(
            UUID.ramdomUUID(),
            request.getNombre(),
            request.getDescripcion(),
            padre
        );

        return categoriaRepository.save(categoria);
    }
    ESTE METODO SE ACTUALIZO*/

    public Producto actualizarCategoria(UUID id, CategoriaRequest request) {

        Categoria categoria = buscarCategoriaPorId(id);

        categoria.actualizarNombre(request.getNombre());
        categoria.actualizarDescripcion(request.getDescripcion());

        if (request.getCategoriaPadreId() != null) {
            Categoria padre = buscarCategoriaPorId(request.getCategoriaPadreId());
            categoria.asignarPadre(padre);
        }

        return categoriaRepository.save(categoria);
    }

    public List<Producto> buscarTodasCategorias() {
        return categoriaRepository.findAll();
    }

    public Producto buscarCategoriaPorId(UUID id) {
        return categoriaRepository.findById(id)
        .orElseThrow(() -> new DomainException("Categoria no encontrado"));
    }
}