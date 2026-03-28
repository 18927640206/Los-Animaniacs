package com.uamishop.shared.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxEvent {

    @Id
    private UUID id;

    private UUID aggregateId;
    private String aggregateType;
    private String type;

    @Lob
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private int retries;
    private String error;
    private Instant createdAt;

    public OutboxEvent() {}

    public OutboxEvent(UUID id, UUID aggregateId, String aggregateType,
                       String type, String payload, OutboxStatus status,
                       int retries, String error, Instant createdAt) {

        this.id = id;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.type = type;
        this.payload = payload;
        this.status = status;
        this.retries = retries;
        this.error = error;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getAggregateId() { return aggregateId; }
    public String getType() { return type; }
    public String getPayload() { return payload; }
    public OutboxStatus getStatus() { return status; }
    public int getRetries() { return retries; }

    public void setStatus(OutboxStatus status) { this.status = status; }
    public void setRetries(int retries) { this.retries = retries; }
    public void setError(String error) { this.error = error; }
}