package com.uamishop.catalogo.shared.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uamishop.catalogo.shared.domain.OutboxEvent;
import com.uamishop.catalogo.shared.domain.OutboxStatus;
import com.uamishop.catalogo.shared.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OutboxService {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void guardarEvento(UUID aggregateId, String aggregateType, String type, Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);

            OutboxEvent event = new OutboxEvent(
                    UUID.randomUUID(),
                    aggregateId,
                    aggregateType,
                    type,
                    json,
                    OutboxStatus.PENDING,
                    0,
                    null,
                    Instant.now()
            );

            repository.save(event);

        } catch (Exception e) {
            throw new RuntimeException("Error serializando evento", e);
        }
    }
}