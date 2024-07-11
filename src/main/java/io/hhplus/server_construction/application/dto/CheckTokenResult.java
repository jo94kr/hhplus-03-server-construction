package io.hhplus.server_construction.application.dto;

import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;

import java.time.LocalDateTime;

public record CheckTokenResult(

        String token,
        Long timeRemainingMinutes,
        Long waitingNumber,
        WaitingStatus status,
        LocalDateTime expireDatetime
) {

    public static CheckTokenResult create(String token,
                                          Long timeRemainingMinutes,
                                          Long waitingNumber,
                                          WaitingStatus status,
                                          LocalDateTime expireDatetime) {
        return new CheckTokenResult(token, timeRemainingMinutes, waitingNumber, status, expireDatetime);
    }
}
