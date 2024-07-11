package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.infra.entity.ConcertEntity;
import io.hhplus.server_construction.infra.entity.ConcertScheduleEntity;
import io.hhplus.server_construction.infra.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {

    List<ConcertSeatEntity> findAllByConcertSchedule_ConcertAndConcertSchedule(ConcertEntity concert, ConcertScheduleEntity concertSchedule);
}
