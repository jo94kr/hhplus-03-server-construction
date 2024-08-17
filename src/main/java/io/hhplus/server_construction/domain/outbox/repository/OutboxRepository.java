package io.hhplus.server_construction.domain.outbox.repository;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxRepository {

    Outbox save(Outbox outbox);

    Outbox findById(String id);

    List<Outbox> findAllByStatusTargetDatetime(OutboxStatus outboxStatus, LocalDateTime targetDatetime);
}
