package io.hhplus.server_construction.presentation.user.dto;

import java.math.BigDecimal;

public record AmountDto(

) {

    public record Response(
            BigDecimal amount
    ) {

    }
}
