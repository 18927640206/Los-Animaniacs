package com.uamishop.ventas.listener;

import com.uamishop.shared.event.OrdenCreadaEvent;
import com.uamishop.ventas.service.CarritoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrdenCreadaListener {

    private final CarritoService carritoService;

    public OrdenCreadaListener(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // --- PASO 3: COMUNICACIÓN ASÍNCRONA ---
    // Escucha el mensaje enviado por el microservicio de Órdenes
    @RabbitListener(queues = "ventas.orden-creada")
    public void handle(OrdenCreadaEvent event) {
        carritoService.completarCheckout(event.carritoId());
    }
}