package io.hhplus.server_construction.presentation.reservation;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.application.reservation.facade.ReservationFacade;
import io.hhplus.server_construction.presentation.reservation.dto.ReservationConcert;
import io.hhplus.server_construction.presentation.user.dto.AmountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "/reservations", description = "예약")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @Operation(summary = "콘서트 좌석 예약")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationConcert.Response.class)))
    @PostMapping()
    public ResponseEntity<ReservationConcert.Response> reservationConcert(@Schema(name = "대기열 토큰")
                                                                          @RequestHeader("token") String token,
                                                                          @RequestBody ReservationConcert.Request request) {
        return ResponseEntity.ok(ReservationConcert.Response.from(
                reservationFacade.reservationConcert(new ReservationConcertCommand(request.concertSeatIdList(), request.userId(), token)))
        );
    }
}
