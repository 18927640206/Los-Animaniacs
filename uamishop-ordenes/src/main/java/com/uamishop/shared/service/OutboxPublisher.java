package com.uamishop.shared.service;

import com.uamishop.shared.domain.OutboxEvent;
import com.uamishop.shared.domain.OutboxStatus;
import com.uamishop.shared.repository.OutboxEventRepository;
import com.uamishop.config.RabbitConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publicarEventos() {

        List<OutboxEvent> eventos = repository.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent e : eventos) {
            try {

                // Routing según tipo
                if (e.getType().equals("OrdenCreada")) {
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EVENTS_EXCHANGE,
                            RabbitConfig.RK_ORDEN_CREADA,
                            e.getPayload()
                    );
                }

                if (e.getType().equals("ProductoComprado")) {
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EVENTS_EXCHANGE,
                            RabbitConfig.RK_PRODUCTO_COMPRADO,
                            e.getPayload()
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