package io.hhplus.server_construction.presentation.payment;

import io.hhplus.server_construction.application.payment.facade.PaymentFacade;
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

    private final PaymentFacade paymentFacade;

    @PostMapping()
    public ResponseEntity<PaymentDto.Response> payment(@RequestHeader("token") String token,
                                                       @RequestBody PaymentDto.Request request) {
        return ResponseEntity.ok(PaymentDto.Response.from(paymentFacade.payment(request.reservationId(), request.userId(), token)));
    }
}
