package com.uamishop.ordenes.service;

import com.uamishop.ordenes.api.OrdenApi;
import com.uamishop.ordenes.api.OrdenResumen;
import com.uamishop.ordenes.domain.*;
import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
import com.uamishop.ordenes.repository.OrdenJpaRepository;
import com.uamishop.ventas.api.VentasApi;
import com.uamishop.ventas.api.CarritoResumen;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.shared.domain.ClienteId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService implements OrdenApi {

    private final OrdenJpaRepository ordenRepository;
    private final VentasApi ventasApi; // Comunicación inter-módulo vía interfaz

    public OrdenService(OrdenJpaRepository ordenRepository, VentasApi ventasApi) {
        this.ordenRepository = ordenRepository;
        this.ventasApi = ventasApi;
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenResumen obtenerResumen(UUID id) {
        Orden orden = buscarPorId(id);
        return new OrdenResumen(
            UUID.fromString(orden.getId().getId()), 
            orden.getEstado().toString(),
            orden.calcularTotal().getMonto()
        );
    }

    // Método solicitado por la Práctica 5: Crear orden desde el flujo de Ventas
    @Transactional
    public Orden crearDesdeCarrito(UUID carritoId, DireccionEnvio direccion) {
        // 1. Obtenemos los datos del carrito de forma segura vía API pública
        CarritoResumen carrito = ventasApi.obtenerResumen(carritoId);

        // 2. Transformamos los ítems del carrito a ítems de la orden (Preservamos datos históricos)
        List<ItemOrden> itemsOrden = carrito.items().stream()
            .map(item -> new ItemOrden(
                new ItemOrdenId(UUID.randomUUID().toString()),
                new ProductoRef(item.productoId(), item.nombreProducto(), item.sku()),
                item.cantidad(),
                item.precioUnitario()
            ))
            .collect(Collectors.toList());

        // 3. Creamos y persistimos la nueva orden
        Orden orden = new Orden(
            new OrdenId(UUID.randomUUID().toString()),
            carrito.clienteId(),
            itemsOrden,
            direccion
        );
        Orden ordenGuardada = ordenRepository.save(orden);

        // 4. Notificamos al módulo de Ventas para completar el ciclo de vida del carrito
        ventasApi.completarCheckout(carritoId);

        return ordenGuardada;
    }

    @Transactional
    public Orden crear(CrearOrdenRequest request) {
        // Implementación básica para compatibilidad con controladores actuales
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
        return ordenRepository.findById(id.toString())
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Orden> buscarTodas() {
        return ordenRepository.findAll();
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