package com.uamishop.ventas.listener;

import com.uamishop.shared.event.OrdenCreadaEvent;
import com.uamishop.ventas.domain.CarritoId; 
import com.uamishop.ventas.service.CarritoService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrdenCreadaListener {

    private final CarritoService carritoService;

    public OrdenCreadaListener(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @EventListener
    public void handle(OrdenCreadaEvent event) {
        carritoService.completarCheckout(event.carritoId());    }
}