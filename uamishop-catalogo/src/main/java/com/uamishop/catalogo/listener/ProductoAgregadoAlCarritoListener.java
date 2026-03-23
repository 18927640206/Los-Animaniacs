package com.uamishop.catalogo.listener;

import com.uamishop.catalogo.shared.domain.*;
import com.uamishop.catalogo.service.ProductoEstadisticasService;
//import com.uamishop.shared.event.ProductoAgregadoAlCarritoEvent;
import com.uamishop.catalogo.shared.event.ProductoAgregadoAlCarritoEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uamishop.catalogo.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


@Component
public class ProductoAgregadoAlCarritoListener {


    private final ProductoEstadisticasService estadisticasService;


    public ProductoAgregadoAlCarritoListener(ProductoEstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }


    @RabbitListener(queues = RabbitConfig.QUEUE_CATALOGO_PRODUCTO_AGREGADO)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onProductoAgregadoAlCarrito(ProductoAgregadoAlCarritoEvent event) {
        estadisticasService.registrarAgregadoAlCarrito(event.productoId());
    }
}
