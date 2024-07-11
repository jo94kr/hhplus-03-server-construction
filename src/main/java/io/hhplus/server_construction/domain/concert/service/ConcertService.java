package io.hhplus.server_construction.domain.concert.service;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.repoisitory.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public Page<Concert> findConcertList(Pageable pageable, String token) {
        return null;
    }

    public List<ConcertSchedule> findConcertScheduleList(Long concertId, String token) {
        return null;
    }

    public List<ConcertSeat> findAllConcertSeatList(Long concertId, Long concertScheduleId, String token) {
        return null;
    }

    public List<ConcertSeat> findAllConcertSeatListBySeatId(Long concertId, Long concertScheduleId, List<Long> seatIdList) {
        return null;
    }
}
