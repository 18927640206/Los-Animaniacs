package com.uamishop.catalogo.service;

import com.uamishop.catalogo.controller.dto.CategoriaRequest;
import com.uamishop.catalogo.controller.dto.CategoriaResponse;
import com.uamishop.catalogo.controller.dto.ProductoResponse;
import com.uamishop.catalogo.domain.Categoria;
import com.uamishop.catalogo.domain.CategoriaId;
import com.uamishop.catalogo.repository.CategoriaJpaRepository;
import com.uamishop.shared.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
}