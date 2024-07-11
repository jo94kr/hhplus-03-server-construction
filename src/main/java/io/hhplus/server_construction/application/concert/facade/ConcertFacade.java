package io.hhplus.server_construction.application.concert.facade;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    public Page<Concert> findConcertList(Pageable pageable, String token) {
        return concertService.findConcertList(pageable, token);
    }

    public List<ConcertSchedule> findConcertScheduleList(Long concertId, String token) {
        return concertService.findConcertScheduleList(concertId, token);
    }

    public List<ConcertSeat> findConcertSeatList(Long concertId, Long concertScheduleId, String token) {
        return concertService.findAllConcertSeatList(concertId, concertScheduleId, token);
    }
}
