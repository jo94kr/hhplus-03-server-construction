package io.hhplus.server_construction.presentation.waiting.dto;

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
