package io.hhplus.server_construction.presentation.concert.dto;

import io.hhplus.server_construction.application.concert.dto.FindConcertScheduleResult;

import java.time.LocalDateTime;

public record FindConcertScheduleDto(

) {

    public record Response(
            Long concertScheduleId,
            LocalDateTime concertDatetime,
            boolean isSoldOut
    ) {

        public static Response from(FindConcertScheduleResult concertScheduleResult) {
            return new Response(concertScheduleResult.concertScheduleId(),
                    concertScheduleResult.concertDatetime(),
                    concertScheduleResult.isSoldOut());
        }
    }
}
