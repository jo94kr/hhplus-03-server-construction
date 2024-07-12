package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.infra.entity.ConcertEntity;
import io.hhplus.server_construction.infra.entity.ConcertScheduleEntity;
import io.hhplus.server_construction.infra.entity.ConcertSeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ConcertSeatEntity s WHERE s.id = :concertSeatId")
    Optional<ConcertSeatEntity> pessimisticLockFindById(@Param("concertSeatId") Long concertSeatId);

    List<ConcertSeatEntity> findAllByConcertSchedule_ConcertAndConcertSchedule(ConcertEntity concert, ConcertScheduleEntity concertSchedule);
}
