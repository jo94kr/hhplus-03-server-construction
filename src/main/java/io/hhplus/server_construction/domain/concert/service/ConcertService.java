package io.hhplus.server_construction.domain.concert.service;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.repoisitory.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public Page<Concert> findConcertList(Pageable pageable) {
        return concertRepository.findAllConcert(pageable);
    }

    public List<ConcertSchedule> findConcertScheduleList(Long concertId, LocalDate startDate, LocalDate endDate) {
        Concert concert = concertRepository.findConcertById(concertId);
        return concertRepository.findAllConcertSchedule(concert, startDate, endDate);
    }

    public List<ConcertSeat> findAllConcertSeatList(Long concertId, Long concertScheduleId) {
        Concert concert = concertRepository.findConcertById(concertId);
        ConcertSchedule concertSchedule = concertRepository.findConcertScheduleById(concertScheduleId);
        return concertRepository.findAllConcertSeat(concert, concertSchedule);
    }

    public List<ConcertSeat> findAllConcertSeatListBySeatId(Long concertId, Long concertScheduleId, List<Long> seatIdList) {
        return null;
    }
}
