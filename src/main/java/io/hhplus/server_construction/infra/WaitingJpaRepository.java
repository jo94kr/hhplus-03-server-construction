package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.hhplus.server_construction.infra.entity.WaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WaitingJpaRepository extends JpaRepository<WaitingEntity, Long> {

    WaitingEntity findByToken(String token);

    @Query("SELECT m.id FROM WaitingEntity m WHERE m.status = :status ORDER BY m.id ASC")
    Long findLastProceedingWaiting(@Param("status") WaitingStatus status);

    @Query("SELECT count(m.id) FROM WaitingEntity m WHERE m.status != :status AND m.accessDatetime BETWEEN :startTime AND :endTime")
    Long findThroughputPerMinute(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") WaitingStatus status);

    List<WaitingEntity> findAllByStatusAndExpiredDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);

    List<WaitingEntity> findWaitingByStatusAndAccessDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);
}
