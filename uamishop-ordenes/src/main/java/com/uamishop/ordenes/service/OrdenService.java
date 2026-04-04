// uamishop-ordenes/src/main/java/com/uamishop/ordenes/service/OrdenService.java
// uamishop-ordenes/src/main/java/com/uamishop/ordenes/service/OrdenService.java
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
import com.uamishop.shared.domain.Money;
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

import com.uamishop.shared.service.OutboxService;

@Service
public class OrdenService implements OrdenApi {

    private final OrdenJpaRepository ordenRepository;
    private final VentasApi ventasApi;
    private final ApplicationEventPublisher eventPublisher;
    private final RabbitTemplate rabbitTemplate;
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
                        new ProductoRef(
                                new com.uamishop.shared.domain.ProductoId(item.productoId().getId()), 
                                item.nombreProducto(), 
                                item.sku()),
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

        ProductoCompradoEvent productoCompradoEvent = crearEventoProductoComprado(ordenGuardada);
        eventPublisher.publishEvent(productoCompradoEvent); 

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
                request.getDireccionEnvio().getCalle(),
                request.getDireccionEnvio().getColonia(),
                request.getDireccionEnvio().getCiudad(),
                request.getDireccionEnvio().getEstado(),
                request.getDireccionEnvio().getCodigoPostal(),
                request.getDireccionEnvio().getPais(),
                request.getDireccionEnvio().getTelefono()
        );

        List<ItemOrden> itemsOrden = request.getItems().stream()
                .map(item -> new ItemOrden(
                        new ItemOrdenId(UUID.randomUUID().toString()),
                        new ProductoRef(
                                new com.uamishop.shared.domain.ProductoId(item.getProductoId()), 
                                "Producto Web", 
                                "WEB-123" 
                        ),
                        item.getCantidad(),
                        new Money(item.getPrecioUnitario(), "MXN") 
                )).collect(Collectors.toList());

        Orden orden = new Orden(
                new OrdenId(UUID.randomUUID().toString()),
                new ClienteId(request.getClienteId()),
                itemsOrden,
                direccion
        );

        Orden ordenGuardada = ordenRepository.save(orden);

        ProductoCompradoEvent event = crearEventoProductoComprado(ordenGuardada);
        eventPublisher.publishEvent(event); 

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
                .map(item -> {
                    UUID prodId;
                    try {
                        prodId = UUID.fromString(item.getProductoRef().getProductoId().getId());
                    } catch (Exception e) {
                        prodId = UUID.randomUUID(); // Escudo: Si el navegador envía basura, usamos un UUID seguro
                    }
                    return new ProductoCompradoEvent.ItemComprado(
                            prodId,
                            item.getProductoRef().getSku(),
                            item.getCantidad(),
                            item.getPrecioUnitario().getMonto(),
                            item.getPrecioUnitario().getMoneda()
                    );
                }).toList();

        UUID cliId;
        try {
            cliId = UUID.fromString(orden.getClienteId().getId());
        } catch (Exception e) {
            cliId = UUID.randomUUID(); // Escudo: Previene el crash del Cliente ID
        }

        return new ProductoCompradoEvent(
                UUID.randomUUID(),
                Instant.now(),
                UUID.fromString(orden.getId().getId()),
                cliId,
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