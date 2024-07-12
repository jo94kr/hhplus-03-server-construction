package io.hhplus.server_construction.presentation.concert.dto;

import io.hhplus.server_construction.domain.concert.Concert;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FindConcertListDto(

) {

    public record Response(
            
            @Schema(name = "콘서트 Id")
            Long concertId,

            @Schema(name = "콘서트 명")
            String name,

            @Schema(name = "콘서트 생성일")
            LocalDateTime createDatetime,

            @Schema(name = "콘서트 수정일")
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
