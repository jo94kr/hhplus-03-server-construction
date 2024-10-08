package io.hhplus.server_construction.application.concert.facade;

import io.hhplus.server_construction.application.concert.dto.FindConcertScheduleResult;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class ConcertFacade {

    private final ConcertService concertService;

    public Page<Concert> findConcertList(Pageable pageable) {
        return concertService.findConcertListWithCache(pageable);
    }

    public List<FindConcertScheduleResult> findConcertScheduleList(Long concertId, LocalDate startDate, LocalDate endDate) {
        return concertService.findConcertScheduleList(concertId, startDate, endDate).stream()
                .map(FindConcertScheduleResult::create)
                .toList();
    }

    public List<ConcertSeat> findAvailableConcertSeat(Long concertScheduleId) {
        return concertService.findAvailableConcertSeatList(concertScheduleId, ConcertSeatStatus.POSSIBLE);
    }
}
