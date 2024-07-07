package io.hhplus.server_construction.presentation.concert.dto;

import java.time.LocalDateTime;

public record FindConcertScheduleDto(

) {

    public record Response(
            Long concertScheduleId,
            LocalDateTime concertDatetime,
            boolean isSoldOut
    ) {

    }
}
