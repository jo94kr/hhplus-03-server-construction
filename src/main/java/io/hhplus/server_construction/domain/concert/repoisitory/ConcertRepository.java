package io.hhplus.server_construction.domain.concert.repoisitory;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ConcertRepository {

    Concert findConcertById(Long concertId);

    Page<Concert> findAllConcert(Pageable pageable);

    List<ConcertSchedule> findAllConcertSchedule(Concert concert, LocalDate startDate, LocalDate endDate);

    ConcertSchedule findConcertScheduleById(Long concertScheduleId);

    List<ConcertSeat> findAllConcertSeat(Concert concert, ConcertSchedule concertSchedule);

    ConcertSeat pessimisticLockFindById(Long concertSeatId);

    ConcertSeat saveConcertSeat(ConcertSeat concertSeat);

    void saveAllConcertSeat(List<ConcertSeat> concertSeatList);
}
