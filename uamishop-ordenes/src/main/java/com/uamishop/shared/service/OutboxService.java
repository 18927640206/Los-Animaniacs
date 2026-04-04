// uamishop-ordenes/src/main/java/com/uamishop/shared/service/OutboxService.java
package com.uamishop.shared.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uamishop.shared.domain.OutboxEvent;
import com.uamishop.shared.domain.OutboxStatus;
import com.uamishop.shared.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

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
                    LocalDateTime.now()
            );

            repository.save(event);

        } catch (Exception e) {
            throw new RuntimeException("Error serializando evento", e);
        }
    }
}