package com.uamishop.shared.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uamishop.shared.domain.OutboxEvent;
import com.uamishop.shared.domain.OutboxStatus;
import com.uamishop.shared.repository.OutboxEventRepository;
import com.uamishop.ventas.config.RabbitConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(OutboxEventRepository repository,
                           RabbitTemplate rabbitTemplate,
                           ObjectMapper objectMapper) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    public void publicarEventos() {

        List<OutboxEvent> eventos = repository.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent e : eventos) {
            try {

                Object payload = objectMapper.readValue(e.getPayload(), Object.class);

                // 🔥 EVENTOS DE VENTAS
                if (e.getType().equals("CarritoVaciado")) {
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EVENTS_EXCHANGE,
                            "carrito.vaciado",
                            payload
                    );
                }

                if (e.getType().equals("VentaConfirmada")) {
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EVENTS_EXCHANGE,
                            "venta.confirmada",
                            payload
                    );
                }

                e.setStatus(OutboxStatus.SENT);

            } catch (Exception ex) {
                e.setStatus(OutboxStatus.FAILED);
                e.setRetries(e.getRetries() + 1);
                e.setError(ex.getMessage());
            }

            repository.save(e);
        }
    }
}