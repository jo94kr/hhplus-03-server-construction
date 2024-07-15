package io.hhplus.server_construction.interfaces.controller.concert.dto;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;

import java.math.BigDecimal;

public record FindConcertSeatDto(

) {

    public record Response(
            Long concertSeatId,
            ConcertSeatGrade grade,
            BigDecimal price,
            ConcertSeatStatus status
    ) {

        public static Response from(ConcertSeat concertSeat) {
            return new Response(concertSeat.getId(),
                    concertSeat.getGrade(),
                    concertSeat.getPrice(),
                    concertSeat.getStatus());
        }
    }
}
