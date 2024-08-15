package io.hhplus.server_construction.domain.outbox.service;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.repository.OutboxRepository;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    @Transactional
    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    public Outbox findById(String id) {
        return outboxRepository.findById(id);
    }

    public List<Outbox> findAllByStatusTargetDatetime(OutboxStatus outboxStatus, LocalDateTime targetDatetime) {
        return outboxRepository.findAllByStatusTargetDatetime(outboxStatus, targetDatetime);
    }
}
