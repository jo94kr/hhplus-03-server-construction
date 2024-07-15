package io.hhplus.server_construction.interfaces.controller.user.dto;

import java.math.BigDecimal;

public record AmountDto(

) {

    public record Response(
            BigDecimal amount
    ) {

    }
}
