package com.uamishop.catalogo.shared.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uamishop.catalogo.shared.domain.OutboxEvent;
import com.uamishop.catalogo.shared.domain.OutboxStatus;
import com.uamishop.catalogo.shared.repository.OutboxEventRepository;
import com.uamishop.catalogo.config.RabbitConfig;

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

                //  EVENTO DE CATALOGO
                if (e.getType().equals("StockActualizado")) {
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EVENTS_EXCHANGE,
                            "stock.actualizado",
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