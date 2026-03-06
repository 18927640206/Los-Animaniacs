package com.uamishop.catalogo.listener;

import com.uamishop.catalogo.service.ProductoEstadisticasService;
import com.uamishop.shared.event.ProductoCompradoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener que maneja los eventos cuando se realiza la compra de productos.
 * Actualiza las estadísticas registrando las ventas de cada producto.
 */
@Component
public class ProductoCompradoListener {


    private final ProductoEstadisticasService estadisticasService;


    public ProductoCompradoListener(ProductoEstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    /**
     * Maneja el evento ProductoCompradoEvent.
     * Se ejecuta de forma asíncrona y en una transacción independiente
     * para que un error en las estadísticas no afecte la compra principal.
     *
     * @param event evento que contiene los productos comprados
     */
    @EventListener
    @Async // El listener se ejecuta en un hilo distinto, las métricas son eventualmente consistentes
    @Transactional(propagation = Propagation.REQUIRES_NEW) // Manejamos una transacción distinta (secundaria) pues una falla aquí no debe afectar ni bloquear la transacción principal
    public void onProductoComprado(ProductoCompradoEvent event) {
        event.items().forEach(item ->
                estadisticasService.registrarVenta(item.productoId(), item.cantidad())
        );
    }
}
