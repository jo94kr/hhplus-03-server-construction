package io.hhplus.server_construction.interfaces.controller.reservation;

import io.hhplus.server_construction.application.reservation.facade.ReservationFacade;
import io.hhplus.server_construction.interfaces.controller.reservation.dto.ReservationConcert;
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
    public ResponseEntity<ReservationConcert.Response> reservationConcert(@RequestBody ReservationConcert.Request request) {
        return ResponseEntity.ok(ReservationConcert.Response.from(
                reservationFacade.reservationConcert(request.toCommand()))
        );
    }
}
