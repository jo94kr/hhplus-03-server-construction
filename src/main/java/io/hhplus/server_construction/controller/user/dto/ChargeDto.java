package io.hhplus.server_construction.controller.user.dto;

import java.math.BigDecimal;

public record ChargeDto(

) {

    public record Request(
            BigDecimal amount
    ) {

    }

    public record Response(
            BigDecimal amount
    ) {

    }
}
