package com.uamishop.ordenes.service;

import com.uamishop.ordenes.domain.*;
import com.uamishop.ordenes.repository.OrdenJpaRepository;
import com.uamishop.ventas.service.CarritoService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrdenService {

    private final OrdenJpaRepository ordenRepository;
    private final CarritoService carritoService;

    public OrdenService(OrdenJpaRepository ordenRepository,
            CarritoService carritoService
        ) {
        this.ordenRepository = ordenRepository;
        this.carritoService = carritoService;
    }

    public Orden crear(OrdenRequest request) {

        Orden orden = Orden.crear(
            request.getClienteId(),
            request.getItems(),
            request.getDireccionEnvio()
        );

        return ordenRepository.save(orden);
    }

    public Orden crearDesdeCarrito(UUID carritoId,
            DireccionEnvio direccionEnvio
        ) {

        var carrito = carritoService.obtenerCarrito(carritoId);

        Orden orden = Orden.crearDesdeCarrito(carrito, direccionEnvio);

        ordenRepository.save(orden);

        carritoService.completarCheckout(carritoId);

        return orden;
    }

    public Orden buscarPorId(UUID id) {
        return ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public Orden buscarTodas() {
        return ordenRepository.findAll();
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
