package com.uamishop.ordenes.service;

import com.uamishop.ordenes.api.OrdenApi;
import com.uamishop.ordenes.api.OrdenResumen;
import com.uamishop.ordenes.domain.*;
import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
import com.uamishop.ordenes.repository.OrdenJpaRepository;
import com.uamishop.ventas.api.VentasApi;
import com.uamishop.shared.domain.ClienteId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
public class OrdenService implements OrdenApi{

    private final OrdenJpaRepository ordenRepository;
    private final VentasApi ventasApi;

    public OrdenService(OrdenJpaRepository ordenRepository, VentasApi ventasApi) {
        this.ordenRepository = ordenRepository;
        this.ventasApi = ventasApi;
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenResumen obtenerResumen(UUID id) {
        Orden orden = buscarPorId(id);
        return new OrdenResumen(
            UUID.fromString(orden.getId().toString()),
            orden.getEstado().toString(),
            orden.calcularTotal().getMonto()
        );
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public Orden buscarPorId(UUID id) {
        return ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Orden> buscarTodas() {
        return ordenRepository.findAll(); // Corregido el tipo de retorno
    }

    @Transactional
    public Orden confirmar(UUID id) {
        Orden orden = buscarPorId(id);
        orden.confirmar();
        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden procesarPago(UUID id, String referencia) {
        Orden orden = buscarPorId(id);
        orden.procesarPago(referencia);
        return ordenRepository.save(orden);
    }

    @Override
    @Transactional
    public void cancelar(UUID id, String motivo) {
        Orden orden = buscarPorId(id);
        orden.cancelar(motivo);
        ordenRepository.save(orden);
    }
}