package com.uamishop.shared.repository;

import com.uamishop.shared.domain.OutboxEvent;
import com.uamishop.shared.domain.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatus(OutboxStatus status);
}