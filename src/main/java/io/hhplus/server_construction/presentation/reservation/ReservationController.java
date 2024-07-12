package io.hhplus.server_construction.presentation.reservation;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.application.reservation.facade.ReservationFacade;
import io.hhplus.server_construction.presentation.reservation.dto.ReservationConcert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping()
    public ResponseEntity<ReservationConcert.Response> reservationConcert(@RequestHeader("token") String token,
                                                                          @RequestBody ReservationConcert.Request request) {
        return ResponseEntity.ok(ReservationConcert.Response.from(
                reservationFacade.reservationConcert(new ReservationConcertCommand(request.concertSeatIdList(), request.userId(), token)))
        );
    }
}
