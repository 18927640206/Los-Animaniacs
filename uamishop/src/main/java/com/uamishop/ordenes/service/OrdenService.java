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
import com.uamishop.config.RabbitConfig; // <-- IMPORTANTE: NUEVA config
import org.springframework.amqp.rabbit.core.RabbitTemplate; // <-- IMPORTANTE TAMBIEN
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import com.uamishop.shared.event.ProductoCompradoEvent;
import com.uamishop.shared.event.OrdenCreadaEvent;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService implements OrdenApi {

    private final OrdenJpaRepository ordenRepository;
    private final VentasApi ventasApi;
    private final ApplicationEventPublisher eventPublisher;
    private final RabbitTemplate rabbitTemplate; // <-- 1. Inyectar RabbitTemplate

    // 2. Actualizar el constructor
    public OrdenService(OrdenJpaRepository ordenRepository, 
                        VentasApi ventasApi, 
                        ApplicationEventPublisher eventPublisher,
                        RabbitTemplate rabbitTemplate) {
        this.ordenRepository = ordenRepository;
        this.ventasApi = ventasApi;
        this.eventPublisher = eventPublisher;
        this.rabbitTemplate = rabbitTemplate;
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

        // Notificamos al módulo de Ventas (Síncrono por ahora)
        ventasApi.completarCheckout(carritoId); 

        // Preparamos el evento de ProductoComprado
        List<ProductoCompradoEvent.ItemComprado> itemsEvent = ordenGuardada.getItems().stream()
            .map(item -> new ProductoCompradoEvent.ItemComprado(
                UUID.fromString(item.getProductoRef().getProductoId().getId()),
                item.getProductoRef().getSku(),
                item.getCantidad(),
                item.getPrecioUnitario().getMonto(),
                item.getPrecioUnitario().getMoneda()
            )).toList();

        ProductoCompradoEvent productoCompradoEvent = new ProductoCompradoEvent(
            UUID.randomUUID(),
            Instant.now(),
            UUID.fromString(ordenGuardada.getId().getId()),
            UUID.fromString(ordenGuardada.getClienteId().getId()),
            itemsEvent
        );

        //Publicar OrdenCreadaEvent (para Ventas - Paso 3)
        eventPublisher.publishEvent(new OrdenCreadaEvent(
            UUID.randomUUID(),
            Instant.now(),
            UUID.fromString(ordenGuardada.getId().getId()),
            carritoId,
            UUID.fromString(ordenGuardada.getClienteId().getId())
        ));

        return ordenGuardada;
    }

    @Transactional
    public Orden crear(CrearOrdenRequest request) {
        DireccionEnvio direccion = new DireccionEnvio("Calle", "Colonia", "Ciudad", "Estado", "12345", "México", "1234567890");
        Orden orden = new Orden(
            new OrdenId(UUID.randomUUID().toString()), 
            new ClienteId(request.getClienteId()), 
            new java.util.ArrayList<>(), 
            direccion
        );
        
        Orden ordenGuardada = ordenRepository.save(orden);

        List<ProductoCompradoEvent.ItemComprado> itemsEvent = ordenGuardada.getItems().stream()
            .map(item -> new ProductoCompradoEvent.ItemComprado(
                UUID.fromString(item.getProductoRef().getProductoId().getId()),
                item.getProductoRef().getSku(),
                item.getCantidad(),
                item.getPrecioUnitario().getMonto(),
                item.getPrecioUnitario().getMoneda()
            )).toList();

        ProductoCompradoEvent event = new ProductoCompradoEvent(
            UUID.randomUUID(),
            Instant.now(),
            UUID.fromString(ordenGuardada.getId().getId()),
            UUID.fromString(ordenGuardada.getClienteId().getId()),
            itemsEvent
        );

        // --- 4. PUBLICACIÓN DUAL TAMBIÉN AQUÍ ---
        eventPublisher.publishEvent(event);
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.EVENTS_EXCHANGE,
            RabbitConfig.RK_PRODUCTO_COMPRADO,
            event
        );

        return ordenGuardada;
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




// package com.uamishop.ordenes.service;

// import com.uamishop.ordenes.api.OrdenApi;
// import com.uamishop.ordenes.api.OrdenResumen;
// import com.uamishop.ordenes.domain.*;
// import com.uamishop.ordenes.controller.dto.CrearOrdenRequest;
// import com.uamishop.ordenes.repository.OrdenJpaRepository;
// import com.uamishop.ventas.api.VentasApi;
// import com.uamishop.ventas.api.CarritoResumen;
// import com.uamishop.ventas.domain.ProductoRef;
// import com.uamishop.shared.domain.ClienteId;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// //para eventos p6
// import org.springframework.context.ApplicationEventPublisher;
// import java.time.Instant;
// import com.uamishop.shared.event.ProductoCompradoEvent;
// import com.uamishop.shared.event.ProductoAgregadoAlCarritoEvent;
// import com.uamishop.shared.event.OrdenCreadaEvent;

// import java.util.UUID;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class OrdenService implements OrdenApi {

//     private final OrdenJpaRepository ordenRepository;
//     private final VentasApi ventasApi; // Comunicación inter-módulo vía interfaz
//     private final ApplicationEventPublisher eventPublisher;

//     public OrdenService(OrdenJpaRepository ordenRepository, VentasApi ventasApi, ApplicationEventPublisher eventPublisher) {
//         this.ordenRepository = ordenRepository;
//         this.ventasApi = ventasApi;
//         this.eventPublisher = eventPublisher;
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public OrdenResumen obtenerResumen(UUID id) {
//         Orden orden = buscarPorId(id);
//         return new OrdenResumen(
//             UUID.fromString(orden.getId().getId()), 
//             orden.getEstado().toString(),
//             orden.calcularTotal().getMonto()
//         );
//     }

//     // Método solicitado por la Práctica 5: Crear orden desde el flujo de Ventas
//     @Transactional
//     public Orden crearDesdeCarrito(UUID carritoId, DireccionEnvio direccion) {
//         // 1. Obtenemos los datos del carrito de forma segura vía API pública
//         CarritoResumen carrito = ventasApi.obtenerResumen(carritoId);

//         // 2. Transformamos los ítems del carrito a ítems de la orden (Preservamos datos históricos)
//         List<ItemOrden> itemsOrden = carrito.items().stream()
//             .map(item -> new ItemOrden(
//                 new ItemOrdenId(UUID.randomUUID().toString()),
//                 new ProductoRef(item.productoId(), item.nombreProducto(), item.sku()),
//                 item.cantidad(),
//                 item.precioUnitario()
//             )).collect(Collectors.toList());

//         // 3. Creamos y persistimos la nueva orden
//         Orden orden = new Orden(
//             new OrdenId(UUID.randomUUID().toString()),
//             carrito.clienteId(),
//             itemsOrden,
//             direccion
//         );
//         Orden ordenGuardada = ordenRepository.save(orden);

//         // 4. Notificamos al módulo de Ventas para completar el ciclo de vida del carrito
//         ventasApi.completarCheckout(carritoId); //->SE tendra que borrar para el paso 3 segun parece para desacoplar los modulos.

//         //Publicar ProductoCompradoEvent (para Catálogo)
//         List<ProductoCompradoEvent.ItemComprado> itemsEvent = ordenGuardada.getItems().stream()
//             .map(item -> new ProductoCompradoEvent.ItemComprado(
//                 // Accedemos a través de productoRef
//                 UUID.fromString(item.getProductoRef().getProductoId().getId()),
//                 item.getProductoRef().getSku(),
//                 item.getCantidad(),
//                 item.getPrecioUnitario().getMonto(),
//                 item.getPrecioUnitario().getMoneda()
//             )).toList();

//         eventPublisher.publishEvent(new ProductoCompradoEvent(
//             UUID.randomUUID(),
//             java.time.Instant.now(),
//             UUID.fromString(ordenGuardada.getId().getId()),
//             UUID.fromString(ordenGuardada.getClienteId().getId()),
//             itemsEvent
//         ));

//         //Publicar OrdenCreadaEvent (para Ventas - Paso 3)
//         eventPublisher.publishEvent(new OrdenCreadaEvent(
//             UUID.randomUUID(),
//             java.time.Instant.now(),
//             UUID.fromString(ordenGuardada.getId().getId()),
//             carritoId,
//             UUID.fromString(ordenGuardada.getClienteId().getId())
//         ));

//         return ordenGuardada;
//     }

//     @Transactional
//     public Orden crear(CrearOrdenRequest request) {
//         DireccionEnvio direccion = new DireccionEnvio("Calle", "Colonia", "Ciudad", "Estado", "12345", "México", "1234567890");
//         Orden orden = new Orden(
//             new OrdenId(UUID.randomUUID().toString()), 
//             new ClienteId(request.getClienteId()), 
//             new java.util.ArrayList<>(), 
//             direccion
//         );
        
//         Orden ordenGuardada = ordenRepository.save(orden);

//         // Notificar a Catálogo 
//         List<ProductoCompradoEvent.ItemComprado> itemsEvent = ordenGuardada.getItems().stream()
//             .map(item -> new ProductoCompradoEvent.ItemComprado(
//                 UUID.fromString(item.getProductoRef().getProductoId().getId()),
//                 item.getProductoRef().getSku(),
//                 item.getCantidad(),
//                 item.getPrecioUnitario().getMonto(),
//                 item.getPrecioUnitario().getMoneda()
//             )).toList();

//         eventPublisher.publishEvent(new ProductoCompradoEvent(
//             UUID.randomUUID(),
//             Instant.now(),
//             UUID.fromString(ordenGuardada.getId().getId()),
//             UUID.fromString(ordenGuardada.getClienteId().getId()),
//             itemsEvent
//         ));

//         return ordenGuardada;
//     }

//     @Transactional(readOnly = true)
//     public Orden buscarPorId(UUID id) {
//         return ordenRepository.findById(id.toString())
//                 .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
//     }

//     @Transactional(readOnly = true)
//     public List<Orden> buscarTodas() {
//         return ordenRepository.findAll();
//     }

//     @Transactional
//     public Orden confirmar(UUID id) {
//         Orden orden = buscarPorId(id);
//         orden.confirmar();
//         return ordenRepository.save(orden);
//     }

//     @Transactional
//     public Orden procesarPago(UUID id, String referencia) {
//         Orden orden = buscarPorId(id);
//         orden.procesarPago(referencia);
//         return ordenRepository.save(orden);
//     }

//     @Override
//     @Transactional
//     public void cancelar(UUID id, String motivo) {
//         Orden orden = buscarPorId(id);
//         orden.cancelar(motivo);
//         ordenRepository.save(orden);
//     }
// }