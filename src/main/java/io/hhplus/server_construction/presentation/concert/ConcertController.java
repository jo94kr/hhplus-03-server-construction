package io.hhplus.server_construction.presentation.concert;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
public class ConcertController {

    @GetMapping()
    public ResponseEntity<Page<FindConcertListDto.Response>> findConcertList(@RequestHeader("token") String token,
                                                                             Pageable pageable) {
        List<FindConcertListDto.Response> responseList = Collections.singletonList(new FindConcertListDto.Response(1L, "항플 콘서트", LocalDateTime.now(), null));
        return ResponseEntity.ok(new PageImpl<>(responseList));
    }

    @GetMapping(value = "/{concertId}/schedules")
    public ResponseEntity<List<FindConcertScheduleDto.Response>> findConcertSchedule(@RequestHeader("token") String token,
                                                                                     @PathVariable(name = "concertId") Long concertId) {
        return ResponseEntity.ok(List.of(new FindConcertScheduleDto.Response(1L,
                LocalDateTime.now().plusDays(1),
                false)));
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
