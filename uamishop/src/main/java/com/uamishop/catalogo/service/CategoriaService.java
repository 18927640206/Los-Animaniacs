package com.uamishop.catalogo.service;

import com.uamishop.catalogo.controller.dto.CategoriaRequest;
import com.uamishop.catalogo.controller.dto.CategoriaResponse;
import com.uamishop.catalogo.controller.dto.ProductoResponse;
import com.uamishop.catalogo.domain.Categoria;
import com.uamishop.shared.domain.CategoriaId;
//import com.uamishop.catalogo.domain.CategoriaId;
import com.uamishop.catalogo.domain.Producto;
import com.uamishop.catalogo.repository.CategoriaJpaRepository;
import com.uamishop.catalogo.repository.ProductoJpaRepository;
import com.uamishop.shared.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.uamishop.catalogo.repository.ProductoJpaRepository;

// ... otros imports
import com.uamishop.catalogo.repository.ProductoJpaRepository; // Asegúrate de importar esto

@Service
public class CategoriaService {

    private final CategoriaJpaRepository categoriaRepository;
    private final ProductoJpaRepository productoRepository; // 1. Inyecta el repositorio de productos

    // 2. Actualiza el constructor
    public CategoriaService(CategoriaJpaRepository categoriaRepository, ProductoJpaRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria(
                new CategoriaId(UUID.randomUUID().toString()),
                request.getNombre()
        );
        categoria.actualizarDescripcion(request.getDescripcion());
        
        categoriaRepository.save(categoria);
        return mapearAResponse(categoria);
    }

    public CategoriaResponse actualizar(UUID id, CategoriaRequest request) {
        //Aqui tambien se justa lo del UUID
        CategoriaId catId = new CategoriaId(id.toString());
        Categoria categoria = categoriaRepository.findById(catId)
            .orElseThrow(() -> new DomainException("Categoría no encontrada"));
        
        // Actualizamos los datos usando los métodos del dominio
        categoria.actualizarNombre(request.getNombre());
        categoria.actualizarDescripcion(request.getDescripcion());
        
        categoriaRepository.save(categoria);
        return mapearAResponse(categoria);
    }

    //Aqui habia un error porque pasaba un 'java.util.UUID'
    public CategoriaResponse buscarPorId(UUID id) {
        //Convertimos el UUID a nuestro value Object del dominio
        CategoriaId catId = new CategoriaId(id.toString());

        //buscamos usando el tipo correcto
        Categoria categoria = categoriaRepository.findById(catId) 
            .orElseThrow(() -> new DomainException("Categoría no encontrada"));

        return mapearAResponse(categoria);
    }

    public List<ProductoResponse> obtenerProductos(UUID id) {
        // 3. Implementa la lógica
        List<Producto> productos = productoRepository.findByCategoriaIdRaw(id.toString());
        
        // 4. Mapea la lista de productos a DTOs
        return productos.stream()
            .map(this::mapearProductoAResponse) // Necesitas este método mapeador
            .collect(Collectors.toList());
    }

    public List<CategoriaResponse> obtenerTodas() {
        // Buscamos todas en la BD y las transformamos a Response (DTO)
        return categoriaRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public void eliminar(UUID id) {
        categoriaRepository.deleteById(new CategoriaId(id.toString()));
    }

    
    // 5. Crea este método auxiliar para mapear
    private ProductoResponse mapearProductoAResponse(Producto producto) {
        return new ProductoResponse(
            UUID.fromString(producto.getId().getId()),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio().getMonto(),
            UUID.fromString(producto.getCategoriaId().getId()),
            producto.isDisponible() ? "ACTIVO" : "INACTIVO"
        );
    }
    
    // Este método es la clave para que el JSON salga bien formateado
    private CategoriaResponse mapearAResponse(Categoria categoria) {
        return new CategoriaResponse(
            UUID.fromString(categoria.getId().getId()),
            categoria.getNombre(),
            categoria.getDescripcion()
        );
    }
}


/*@Service
public class CategoriaService {

    private final CategoriaJpaRepository categoriaRepository;

    public CategoriaService(CategoriaJpaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria(
                new CategoriaId(UUID.randomUUID().toString()),
                request.getNombre()
        );
        categoria.actualizarDescripcion(request.getDescripcion());
        
        categoriaRepository.save(categoria);
        return mapearAResponse(categoria);
    }

    public CategoriaResponse actualizar(UUID id, CategoriaRequest request) {
        //Aqui tambien se justa lo del UUID
        CategoriaId catId = new CategoriaId(id.toString());
        Categoria categoria = categoriaRepository.findById(catId)
            .orElseThrow(() -> new DomainException("Categoría no encontrada"));
        
        // Actualizamos los datos usando los métodos del dominio
        categoria.actualizarNombre(request.getNombre());
        categoria.actualizarDescripcion(request.getDescripcion());
        
        categoriaRepository.save(categoria);
        return mapearAResponse(categoria);
    }

    //Aqui habia un error porque pasaba un 'java.util.UUID'
    public CategoriaResponse buscarPorId(UUID id) {
        //Convertimos el UUID a nuestro value Object del dominio
        CategoriaId catId = new CategoriaId(id.toString());

        //buscamos usando el tipo correcto
        Categoria categoria = categoriaRepository.findById(catId) 
            .orElseThrow(() -> new DomainException("Categoría no encontrada"));

        return mapearAResponse(categoria);
    }

    public List<CategoriaResponse> obtenerTodas() {
        // Buscamos todas en la BD y las transformamos a Response (DTO)
        return categoriaRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public void eliminar(UUID id) {
        categoriaRepository.deleteById(new CategoriaId(id.toString()));
    }

    public List<ProductoResponse> obtenerProductos(UUID id) {
        // Implementación pendiente dependiente de la relación en BD
        return new ArrayList<>();
    }

    // Este método es la clave para que el JSON salga bien formateado
    private CategoriaResponse mapearAResponse(Categoria categoria) {
        return new CategoriaResponse(
            UUID.fromString(categoria.getId().getId()),
            categoria.getNombre(),
            categoria.getDescripcion()
        );
    }
}*/