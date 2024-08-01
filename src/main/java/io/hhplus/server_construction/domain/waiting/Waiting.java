package io.hhplus.server_construction.domain.waiting;

import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class Waiting {

    private final Long id;
    private final String token;
    private WaitingStatus status;
    private final Long rank;
    private LocalDateTime accessDatetime;
    private LocalDateTime expiredDatetime;
    private final LocalDateTime createDatetime;

    @Builder
    public Waiting(
            Long id,
            String token,
            WaitingStatus status,
            Long rank,
            LocalDateTime accessDatetime,
            LocalDateTime expiredDatetime,
            LocalDateTime createDatetime
    ) {
        this.id = id;
        this.token = token;
        this.status = status;
        this.rank = rank;
        this.accessDatetime = accessDatetime;
        this.expiredDatetime = expiredDatetime;
        this.createDatetime = createDatetime;
    }

    public static Waiting create() {
        return new Waiting(null,
                UUID.randomUUID().toString(),
                WaitingStatus.WAITING,
                null,
                null,
                LocalDateTime.now().plusMinutes(5),
                null);
    }

    public Waiting renewalExpiredDatetime() {
        this.expiredDatetime = LocalDateTime.now().plusMinutes(5);
        return this;
    }

    public Waiting expireToken() {
        this.status = WaitingStatus.EXPIRED;
        return this;
    }

    public Long getTimeRemainingMinutes() {
        return LocalDateTime.now().until(this.accessDatetime, ChronoUnit.MINUTES);
    }

    public Waiting setRemainingDatetime(LocalDateTime timeRemaining) {
        this.accessDatetime = timeRemaining;
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
