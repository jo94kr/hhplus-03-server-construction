package io.hhplus.server_construction.presentation.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ChargeDto(

) {

    public record Request(
            @Schema(description = "충전금액")
            BigDecimal amount
    ) {

    }

    public record Response(
            @Schema(description = "잔액")
            BigDecimal amount
    ) {

    }
}
