package com.uamishop.catalogo.listener;

import com.uamishop.shared.event.ProductoAgregadoAlCarritoEvent;
import com.uamishop.catalogo.service.ProductoEstadisticasService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductoAgregadoAlCarritoListener {

    private final ProductoEstadisticasService productoEstadisticasService;

    public ProductoAgregadoAlCarritoListener(ProductoEstadisticasService productoEstadisticasService) {
        this.productoEstadisticasService = productoEstadisticasService;
    }

    @EventListener
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW) 
    public void manejarProductoAgregadoAlCarrito(ProductoAgregadoAlCarritoEvent event) {

        productoEstadisticasService.registrarAgregadoAlCarrito(
                event.productoId()
        );
    }
}