package io.hhplus.server_construction.presentation.waiting.dto;

import io.hhplus.server_construction.application.waiting.dto.CheckTokenResult;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CheckWaitingDto(

) {

    public record Response(
            @Schema(description = "대기열 토큰")
            String token,
            @Schema(description = "대기번호")
            Long waitingNumber,
            @Schema(description = "남은시간 (분)")
            Long timeRemainingMinutes,
            @Schema(description = "대기열 상태")
            WaitingStatus status,
            @Schema(description = "만료 일시")
            LocalDateTime expireDatetime
    ) {

        public static Response from(CheckTokenResult checkTokenResult) {
            return new Response(checkTokenResult.token(),
                    checkTokenResult.waitingNumber(),
                    checkTokenResult.timeRemainingMinutes(),
                    checkTokenResult.status(),
                    checkTokenResult.expireDatetime());
        }
    }
}
