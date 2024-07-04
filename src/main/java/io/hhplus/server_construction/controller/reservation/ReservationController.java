package io.hhplus.server_construction.controller.reservation;

import io.hhplus.server_construction.controller.concert.dto.FindConcertScheduleDto;
import io.hhplus.server_construction.controller.reservation.dto.PaymentDto;
import io.hhplus.server_construction.controller.reservation.dto.ReservationConcert;
import io.hhplus.server_construction.domain.reservation.ReservationEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    @PostMapping()
    public ResponseEntity<ReservationConcert.Response> reservationConcert(@RequestHeader("token") String token,
                                                                          @RequestBody ReservationConcert.Request request) {
        ReservationConcert.Response response = new ReservationConcert.Response(1L,
                2L,
                BigDecimal.valueOf(1000L),
                List.of(new ReservationConcert.Response.Item(1L, "A1", BigDecimal.valueOf(1000L))));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentDto.Response> payment(@RequestHeader("token") String token,
                                                       @RequestBody PaymentDto.Request request) {
        return ResponseEntity.ok(new PaymentDto.Response(1L,
                ReservationEnums.PaymentStatus.COMPLETE,
                BigDecimal.valueOf(100L),
                BigDecimal.valueOf(200L)));
    }
}
