package io.hhplus.server_construction.application.concert.dto;

import io.hhplus.server_construction.domain.concert.ConcertSchedule;

import java.time.LocalDateTime;

public record FindConcertScheduleResult(
        Long concertScheduleId,
        LocalDateTime concertDatetime
) {

    public static FindConcertScheduleResult create(ConcertSchedule concertSchedule) {
        return new FindConcertScheduleResult(concertSchedule.getId(),
                concertSchedule.getConcertDatetime());
    }
}
