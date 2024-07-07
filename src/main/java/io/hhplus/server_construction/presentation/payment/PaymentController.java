package io.hhplus.server_construction.presentation.payment;

import io.hhplus.server_construction.domain.payment.PaymentEnums;
import io.hhplus.server_construction.presentation.payment.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    @PostMapping()
    public ResponseEntity<PaymentDto.Response> payment(@RequestHeader("token") String token,
                                                       @RequestBody PaymentDto.Request request) {
        return ResponseEntity.ok(new PaymentDto.Response(1L,
                PaymentEnums.PaymentStatus.COMPLETE,
                BigDecimal.valueOf(100L),
                BigDecimal.valueOf(200L)));
    }
}
