package io.hhplus.server_construction.controller.concert.dto;

import io.hhplus.server_construction.domain.concert.ConcertEnums;

import java.math.BigDecimal;

public record FindConcertSeatDto(

) {

    public record Response(
            Long concertSeatId,
            ConcertEnums.Grade grade,
            BigDecimal price,
            ConcertEnums.Status status
    ) {

    }
}
