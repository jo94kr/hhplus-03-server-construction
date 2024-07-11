package io.hhplus.server_construction.presentation.concert;

import io.hhplus.server_construction.application.concert.facade.ConcertFacade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;
import io.hhplus.server_construction.presentation.concert.dto.FindConcertListDto;
import io.hhplus.server_construction.presentation.concert.dto.FindConcertScheduleDto;
import io.hhplus.server_construction.presentation.concert.dto.FindConcertSeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
public class ConcertController {

    private final ConcertFacade concertFacade;

    @GetMapping()
    public ResponseEntity<Page<FindConcertListDto.Response>> findConcertList(@RequestHeader(name = "token") String token,
                                                                             Pageable pageable) {
        return ResponseEntity.ok(concertFacade.findConcertList(pageable, token)
                .map(FindConcertListDto.Response::from));
    }

    @GetMapping(value = "/{concertId}/schedules")
    public ResponseEntity<List<FindConcertScheduleDto.Response>> findConcertSchedule(@RequestHeader("token") String token,
                                                                                     @PathVariable(name = "concertId") Long concertId,
                                                                                     @RequestParam(name = "startDate")LocalDate startDate,
                                                                                     @RequestParam(name = "endDate")LocalDate endDate) {
        return ResponseEntity.ok(concertFacade.findConcertScheduleList(concertId, token, startDate, endDate).stream()
                .map(FindConcertScheduleDto.Response::from)
                .toList());
    }

    @GetMapping(value = "/{concertId}/schedules/{concertScheduleId}/seats")
    public ResponseEntity<List<FindConcertSeatDto.Response>> findConcertSeat(@RequestHeader("token") String token,
                                                                             @PathVariable(name = "concertId") Long concertId,
                                                                             @PathVariable("concertScheduleId") Long concertScheduleId) {
        return ResponseEntity.ok(List.of(new FindConcertSeatDto.Response(1L,
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000L),
                ConcertSeatEnums.Status.POSSIBLE)));
    }
}
