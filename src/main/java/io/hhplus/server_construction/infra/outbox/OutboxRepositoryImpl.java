package io.hhplus.server_construction.infra.outbox;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.repository.OutboxRepository;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.infra.outbox.entity.OutboxEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(OutboxEntity.toEntity(outbox)).toDomain();
    }

    @Override
    public Outbox findById(String id) {
        return outboxJpaRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new)
                .toDomain();
    }

    @Override
    public List<Outbox> findAllByStatusTargetDatetime(OutboxStatus outboxStatus, LocalDateTime targetDatetime) {
        return outboxJpaRepository.findAllByStatusAndCreateDatetimeBefore(outboxStatus, targetDatetime).stream()
                .map(OutboxEntity::toDomain)
                .toList();
    }
}
