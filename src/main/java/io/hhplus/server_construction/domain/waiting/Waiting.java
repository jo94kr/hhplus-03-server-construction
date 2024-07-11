package io.hhplus.server_construction.domain.waiting;

import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Waiting {

    private final Long id;
    private final String token;
    private WaitingStatus status;
    private LocalDateTime remainingDatetime;
    private LocalDateTime expiredDatetime;
    private final LocalDateTime createDatetime;

    public Waiting(
            Long id,
            String token,
            WaitingStatus status,
            LocalDateTime remainingDatetime,
            LocalDateTime expiredDatetime,
            LocalDateTime createDatetime
    ) {
        this.id = id;
        this.token = token;
        this.remainingDatetime = remainingDatetime;
        this.status = status;
        this.expiredDatetime = expiredDatetime;
        this.createDatetime = createDatetime;
    }

    public static Waiting create() {
        return new Waiting(null,
                UUID.randomUUID().toString(),
                WaitingStatus.WAITING,
                null,
                LocalDateTime.now().plusMinutes(5),
                null);
    }

    public Waiting renewalExpiredDatetime() {
        this.expiredDatetime = this.expiredDatetime.plusMinutes(5);
        return this;
    }

    public Waiting expireToken() {
        this.status = WaitingStatus.EXPIRED;
        return this;
    }

    public Waiting setRemainingDatetime(LocalDateTime timeRemaining) {
        this.remainingDatetime = timeRemaining;
        return this;
    }

    public Waiting remainingToken() {
        this.status = WaitingStatus.PROCEEDING;
        return this;
    }

    public boolean isAvailableToken() {
        return WaitingStatus.PROCEEDING.equals(this.status);
    }
}
