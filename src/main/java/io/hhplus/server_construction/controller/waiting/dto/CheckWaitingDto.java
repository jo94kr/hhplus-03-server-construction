package io.hhplus.server_construction.controller.waiting.dto;

import io.hhplus.server_construction.domain.waiting.WaitingEnums;

public record CheckWaitingDto(

) {

    public record Response(
            String token,
            long rank,
            WaitingEnums status
    ) {

    }
}
