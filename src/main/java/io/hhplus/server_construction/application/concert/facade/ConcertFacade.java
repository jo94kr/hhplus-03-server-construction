package io.hhplus.server_construction.application.concert.facade;

import io.hhplus.server_construction.application.concert.dto.FindConcertScheduleResult;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
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
    private final WaitingService waitingService;

    public Page<Concert> findConcertList(Pageable pageable, String token) {
        if (!waitingService.checkWaitingStatus(token)) {
            throw new TokenExpiredException();
        }
        return concertService.findConcertList(pageable);
    }

    public List<FindConcertScheduleResult> findConcertScheduleList(Long concertId, String token, LocalDate startDate, LocalDate endDate) {
        if (!waitingService.checkWaitingStatus(token)) {
            throw new TokenExpiredException();
        }
        return concertService.findConcertScheduleList(concertId, startDate, endDate).stream()
                .map(FindConcertScheduleResult::create)
                .toList();
    }

    public List<ConcertSeat> findConcertSeatList(Long concertId, Long concertScheduleId, String token) {
        if (!waitingService.checkWaitingStatus(token)) {
            throw new TokenExpiredException();
        }
        return concertService.findAllConcertSeatList(concertId, concertScheduleId);
    }
}
