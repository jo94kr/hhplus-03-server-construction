package io.hhplus.server_construction.presentation.concert.dto;

import io.hhplus.server_construction.domain.concert.Concert;

import java.time.LocalDateTime;

public record FindConcertListDto(

) {

    public record Response(
            Long concertId,
            String name,
            LocalDateTime createDatetime,
            LocalDateTime modifyDatetime
    ) {

        public static Response from(Concert concert) {
            return new Response(concert.getId(),
                    concert.getName(),
                    concert.getCreateDatetime(),
                    concert.getModifyDatetime());
        }
    }
}
