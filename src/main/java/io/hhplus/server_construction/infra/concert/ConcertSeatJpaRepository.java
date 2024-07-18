package io.hhplus.server_construction.infra.concert;

import io.hhplus.server_construction.infra.concert.entity.ConcertEntity;
import io.hhplus.server_construction.infra.concert.entity.ConcertScheduleEntity;
import io.hhplus.server_construction.infra.concert.entity.ConcertSeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {

    Optional<ConcertSeatEntity> findConcertSeatById(Long concertSeatId);

    List<ConcertSeatEntity> findAllByConcertSchedule_ConcertAndConcertSchedule(ConcertEntity concert, ConcertScheduleEntity concertSchedule);
}
