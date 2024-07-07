package io.hhplus.server_construction.presentation.concert.dto;

import java.time.LocalDateTime;

public record FindConcertListDto(

) {

    public record Response(
            Long concertId,
            String name,
            LocalDateTime createDatetime,
            LocalDateTime modifyDatetime
    ) {

    }
}
