package io.hhplus.server_construction.presentation.waiting.dto;

import io.hhplus.server_construction.application.waiting.dto.CheckTokenResult;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;

public record CheckWaitingDto(

) {

    public record Response(
            String token,
            Long waitingNumber,
            WaitingStatus status
    ) {

        public static Response from(CheckTokenResult checkTokenResult) {
            return new Response(checkTokenResult.token(), checkTokenResult.waitingNumber(), checkTokenResult.status());
        }
    }
}
