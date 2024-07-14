package io.hhplus.server_construction.presentation.payment;

import io.hhplus.server_construction.application.payment.facade.PaymentFacade;
import io.hhplus.server_construction.domain.payment.PaymentEnums;
import io.hhplus.server_construction.presentation.payment.dto.PaymentDto;
import io.hhplus.server_construction.presentation.user.dto.AmountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "/payment", description = "결제")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @Operation(summary = "결제")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDto.Request.class)))
    @PostMapping()
    public ResponseEntity<PaymentDto.Response> payment(@Schema(name = "대기열 토큰")
                                                       @RequestHeader("token") String token,
                                                       @RequestBody PaymentDto.Request request) {
        return ResponseEntity.ok(PaymentDto.Response.from(paymentFacade.payment(request.reservationId(), request.userId(), token)));
    }
}
