package io.hhplus.server_construction.interfaces.controller.concert;

import io.hhplus.server_construction.application.concert.facade.ConcertFacade;
import io.hhplus.server_construction.interfaces.controller.concert.dto.FindConcertListDto;
import io.hhplus.server_construction.interfaces.controller.concert.dto.FindConcertScheduleDto;
import io.hhplus.server_construction.interfaces.controller.concert.dto.FindConcertSeatDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
@Tag(name = "/concerts", description = "콘서트")
public class ConcertController {

    private final ConcertFacade concertFacade;

    @Operation(summary = "콘서트 목록 조회")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FindConcertListDto.Response.class)))
    @GetMapping()
    public ResponseEntity<Page<FindConcertListDto.Response>> findConcertList(@Schema(name = "페이징 정보") Pageable pageable) {
        return ResponseEntity.ok(concertFacade.findConcertList(pageable)
                .map(FindConcertListDto.Response::from));
    }

    @Operation(summary = "콘서트 일정 목록 조회")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FindConcertScheduleDto.Response.class)))
    @GetMapping(value = "/{concertId}/schedules")
    public ResponseEntity<List<FindConcertScheduleDto.Response>> findConcertSchedule(
            @Schema(name = "콘서트 Id") @PathVariable(name = "concertId") Long concertId,
            @Schema(name = "날짜 필터 - 시작일") @RequestParam(name = "startDate") LocalDate startDate,
            @Schema(name = "날짜 필터 - 종료일") @RequestParam(name = "endDate") LocalDate endDate) {
        return ResponseEntity.ok(concertFacade.findConcertScheduleList(concertId, startDate, endDate).stream()
                .map(FindConcertScheduleDto.Response::from)
                .toList());
    }

    @Operation(summary = "콘서트 일정 목록 조회")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FindConcertSeatDto.Response.class)))
    @GetMapping(value = "/{concertId}/schedules/{concertScheduleId}/seats")
    public ResponseEntity<List<FindConcertSeatDto.Response>> findConcertSeat(@Schema(name = "콘서트 Id") @PathVariable(name = "concertId") Long concertId,
                                                                             @Schema(name = "콘서트 스케쥴 Id") @PathVariable("concertScheduleId") Long concertScheduleId) {
        return ResponseEntity.ok(concertFacade.findConcertSeatList(concertId, concertScheduleId).stream()
                .map(FindConcertSeatDto.Response::from)
                .toList());
    }
}
