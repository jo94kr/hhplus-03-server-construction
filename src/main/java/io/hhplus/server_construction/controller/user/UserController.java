package io.hhplus.server_construction.controller.user;

import io.hhplus.server_construction.controller.reservation.dto.ReservationConcert;
import io.hhplus.server_construction.controller.user.dto.AmountDto;
import io.hhplus.server_construction.controller.user.dto.ChargeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("{userId}/amount")
    public ResponseEntity<AmountDto.Response> amount(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(new AmountDto.Response(BigDecimal.valueOf(100L)));
    }

    @PatchMapping("{userId}/charge")
    public ResponseEntity<ChargeDto.Response> charge(@PathVariable("userId") Long userId,
                                                     @RequestBody ChargeDto.Request request) {
        return ResponseEntity.ok(new ChargeDto.Response(BigDecimal.valueOf(200L)));
    }
}