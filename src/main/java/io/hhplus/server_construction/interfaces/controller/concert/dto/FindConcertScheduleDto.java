package io.hhplus.server_construction.interfaces.controller.concert.dto;

import io.hhplus.server_construction.application.concert.dto.FindConcertScheduleResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FindConcertScheduleDto(

) {

    public record Response(

            @Schema(name = "콘서트 스케쥴 Id")
            Long concertScheduleId,

            @Schema(name = "콘서트 일시")
            LocalDateTime concertDatetime,

            @Schema(name = "매진 여부")
            boolean isSoldOut
    ) {

        public static Response from(FindConcertScheduleResult concertScheduleResult) {
            return new Response(concertScheduleResult.concertScheduleId(),
                    concertScheduleResult.concertDatetime(),
                    concertScheduleResult.isSoldOut());
        }
    }
}
