package io.hhplus.server_construction.infra.outbox;


import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.infra.outbox.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, String> {

    List<OutboxEntity> findAllByStatusAndModifyDatetimeBefore(OutboxStatus status, LocalDateTime targetDatetime);
}
