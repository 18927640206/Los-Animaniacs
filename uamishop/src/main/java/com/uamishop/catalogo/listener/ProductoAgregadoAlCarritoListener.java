package com.uamishop.catalogo.listener;

import com.uamishop.shared.event.ProductoAgregadoAlCarritoEvent;
import com.uamishop.catalogo.service.ProductoEstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;

/**
 * escucha eventos cuando un producto es agregado al carrito.
 * Al recibir el evento, registra la acción en el servicio de estadísticas.
 */
@Component
@RequiredArgsConstructor
public class ProductoAgregadoAlCarritoListener {

    private final ProductoEstadisticasService productoEstadisticasService;

    /**
     * Maneja el evento ProductoAgregadoAlCarritoEvent y registra
     * el agregado del producto al carrito en las estadísticas.
     *
     * @param event evento que contiene la información del producto agregado
     */
    @EventListener
    @Async
    public void manejarProductoAgregadoAlCarrito(ProductoAgregadoAlCarritoEvent event) {

        productoEstadisticasService.registrarAgregadoAlCarrito(
                event.productoId()
        );
    }
}