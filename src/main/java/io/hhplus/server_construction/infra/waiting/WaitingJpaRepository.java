package io.hhplus.server_construction.infra.waiting;

import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.hhplus.server_construction.infra.waiting.entity.WaitingEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingJpaRepository extends JpaRepository<WaitingEntity, Long> {

    Optional<WaitingEntity> findByToken(String token);

    @Query("SELECT m.id FROM WaitingEntity m WHERE m.status = :status ORDER BY m.id ASC")
    Optional<Long> findWaitingEntity(@Param("status") WaitingStatus status, PageRequest pageRequest);

    List<WaitingEntity> findAllByStatusNotAndExpiredDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);

    List<WaitingEntity> findWaitingByStatusAndAccessDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);
}
