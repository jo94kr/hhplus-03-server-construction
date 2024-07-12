package io.hhplus.server_construction.presentation.concert.dto;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;

import java.math.BigDecimal;

public record FindConcertSeatDto(

) {

    public record Response(
            Long concertSeatId,
            ConcertSeatEnums.Grade grade,
            BigDecimal price,
            ConcertSeatEnums.Status status
    ) {

        public static Response from(ConcertSeat concertSeat) {
            return new Response(concertSeat.getId(),
                    concertSeat.getGrade(),
                    concertSeat.getPrice(),
                    concertSeat.getStatus());
        }
    }
}
