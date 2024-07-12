package io.hhplus.server_construction.presentation.reservation;

import io.hhplus.server_construction.application.reservation.facade.ReservationFacade;
import io.hhplus.server_construction.presentation.reservation.dto.ReservationConcert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping()
    public ResponseEntity<ReservationConcert.Response> reservationConcert(@RequestHeader("token") String token,
                                                                          @RequestBody ReservationConcert.Request request) {
        ReservationConcert.Response response = new ReservationConcert.Response(1L,
                BigDecimal.valueOf(1000L),
                List.of(new ReservationConcert.Response.Item(1L, "A1", BigDecimal.valueOf(1000L))));

        return ResponseEntity.ok(response);
    }
}
