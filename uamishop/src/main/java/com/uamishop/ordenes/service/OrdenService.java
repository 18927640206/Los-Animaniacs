package com.uamishop.ordenes.service;

import com.uamishop.ordenes.domain.*;
import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
import com.uamishop.ordenes.repository.OrdenJpaRepository;
import com.uamishop.ventas.service.CarritoService;
import com.uamishop.shared.domain.ClienteId;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenJpaRepository ordenRepository;
    private final CarritoService carritoService;

    public OrdenService(OrdenJpaRepository ordenRepository, CarritoService carritoService) {
        this.ordenRepository = ordenRepository;
        this.carritoService = carritoService;
    }

    public Orden crear(CrearOrdenRequest request) {
        // Por ahora, simulamos la creación para que compile correctamente.
        // En una app real, aquí se mapearían los items y la dirección desde el DTO.
        DireccionEnvio direccion = new DireccionEnvio("Calle", "Colonia", "Ciudad", "Estado", "12345", "México", "1234567890");
        Orden orden = new Orden(
            new OrdenId(UUID.randomUUID().toString()), 
            new ClienteId(request.getClienteId()), 
            new java.util.ArrayList<>(), 
            direccion
        );
        return ordenRepository.save(orden);
    }

    public Orden buscarPorId(UUID id) {
        return ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public List<Orden> buscarTodas() {
        return ordenRepository.findAll(); // Corregido el tipo de retorno
    }

    public Orden confirmar(UUID id) {
        Orden orden = buscarPorId(id);
        orden.confirmar();
        return ordenRepository.save(orden);
    }

    public Orden procesarPago(UUID id, String referencia) {
        Orden orden = buscarPorId(id);
        orden.procesarPago(referencia);
        return ordenRepository.save(orden);
    }

    public Orden cancelar(UUID id, String motivo) {
        Orden orden = buscarPorId(id);
        orden.cancelar(motivo);
        return ordenRepository.save(orden);
    }
}