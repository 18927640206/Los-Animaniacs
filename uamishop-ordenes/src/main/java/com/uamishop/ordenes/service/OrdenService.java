// Archivo: /workspaces/Los-Animaniacs/uamishop-ordenes/src/main/java/com/uamishop/ordenes/service/OrdenService.java
package com.uamishop.ordenes.service;

import com.uamishop.ordenes.api.OrdenApi;
import com.uamishop.ordenes.api.OrdenResumen;
import com.uamishop.ordenes.domain.*;
import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
import com.uamishop.ordenes.repository.OrdenJpaRepository;
import com.uamishop.ventas.api.VentasApi;
import com.uamishop.ventas.api.CarritoResumen;
import com.uamishop.shared.domain.ProductoRef;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.config.RabbitConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import com.uamishop.shared.event.ProductoCompradoEvent;
import com.uamishop.shared.event.OrdenCreadaEvent;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

//  NUEVO
import com.uamishop.shared.service.OutboxService;

@Service
public class OrdenService implements OrdenApi {

    private final OrdenJpaRepository ordenRepository;
    private final VentasApi ventasApi;
    private final ApplicationEventPublisher eventPublisher;
    private final RabbitTemplate rabbitTemplate;

    //  NUEVO
    private final OutboxService outboxService;

    public OrdenService(OrdenJpaRepository ordenRepository,
                        VentasApi ventasApi,
                        ApplicationEventPublisher eventPublisher,
                        RabbitTemplate rabbitTemplate,
                        OutboxService outboxService) {

        this.ordenRepository = ordenRepository;
        this.ventasApi = ventasApi;
        this.eventPublisher = eventPublisher;
        this.rabbitTemplate = rabbitTemplate;
        this.outboxService = outboxService;
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

    @Transactional
    public Orden crearDesdeCarrito(UUID carritoId, DireccionEnvio direccion) {

        CarritoResumen carrito = ventasApi.obtenerResumen(carritoId);

        List<ItemOrden> itemsOrden = carrito.items().stream()
                .map(item -> new ItemOrden(
                        new ItemOrdenId(UUID.randomUUID().toString()),
                        new ProductoRef(item.productoId(), item.nombreProducto(), item.sku()),
                        item.cantidad(),
                        item.precioUnitario()
                )).collect(Collectors.toList());

        Orden orden = new Orden(
                new OrdenId(UUID.randomUUID().toString()),
                carrito.clienteId(),
                itemsOrden,
                direccion
        );

        Orden ordenGuardada = ordenRepository.save(orden);

        //  EVENTO 1: ORDEN CREADA (ANTES → Rabbit)
        OrdenCreadaEvent ordenEvent = new OrdenCreadaEvent(
                UUID.randomUUID(),
                Instant.now(),
                UUID.fromString(ordenGuardada.getId().getId()),
                carritoId,
                UUID.fromString(ordenGuardada.getClienteId().getId())
        );

        outboxService.guardarEvento(
                UUID.fromString(ordenGuardada.getId().getId()),
                "Orden",
                "OrdenCreada",
                ordenEvent
        );

        // EVENTO 2: PRODUCTO COMPRADO (ANTES → Rabbit)
        ProductoCompradoEvent productoCompradoEvent = crearEventoProductoComprado(ordenGuardada);

        eventPublisher.publishEvent(productoCompradoEvent); // opcional (lo dejamos)

        outboxService.guardarEvento(
                UUID.fromString(ordenGuardada.getId().getId()),
                "Orden",
                "ProductoComprado",
                productoCompradoEvent
        );

        return ordenGuardada;
    }

    @Transactional
    public Orden crear(CrearOrdenRequest request) {

        DireccionEnvio direccion = new DireccionEnvio(
                "Calle", "Colonia", "Ciudad", "Estado",
                "12345", "México", "1234567890"
        );

        Orden orden = new Orden(
                new OrdenId(UUID.randomUUID().toString()),
                new ClienteId(request.getClienteId()),
                new java.util.ArrayList<>(),
                direccion
        );

        Orden ordenGuardada = ordenRepository.save(orden);

        ProductoCompradoEvent event = crearEventoProductoComprado(ordenGuardada);

        eventPublisher.publishEvent(event); // opcional

        // 🔥 SOLO CAMBIO AQUÍ
        outboxService.guardarEvento(
                UUID.fromString(ordenGuardada.getId().getId()),
                "Orden",
                "ProductoComprado",
                event
        );

        return ordenGuardada;
    }

    private ProductoCompradoEvent crearEventoProductoComprado(Orden orden) {

        List<ProductoCompradoEvent.ItemComprado> itemsEvent = orden.getItems().stream()
                .map(item -> new ProductoCompradoEvent.ItemComprado(
                        UUID.fromString(item.getProductoRef().getProductoId().getId()),
                        item.getProductoRef().getSku(),
                        item.getCantidad(),
                        item.getPrecioUnitario().getMonto(),
                        item.getPrecioUnitario().getMoneda()
                )).toList();

        return new ProductoCompradoEvent(
                UUID.randomUUID(),
                Instant.now(),
                UUID.fromString(orden.getId().getId()),
                UUID.fromString(orden.getClienteId().getId()),
                itemsEvent
        );
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