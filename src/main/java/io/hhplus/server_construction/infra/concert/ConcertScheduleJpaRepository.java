package io.hhplus.server_construction.infra.concert;

import io.hhplus.server_construction.infra.concert.entity.ConcertEntity;
import io.hhplus.server_construction.infra.concert.entity.ConcertScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {

    List<ConcertScheduleEntity> findAllByConcertAndConcertDatetimeBetween(ConcertEntity concert, LocalDateTime startDate, LocalDateTime endDate);
}
