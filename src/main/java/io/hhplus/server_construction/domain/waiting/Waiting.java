package io.hhplus.server_construction.domain.waiting;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Waiting {

    private final Long id;
    private final String token;
    private final LocalDateTime expiredDatetime;
    private final LocalDateTime createDatetime;

    private Waiting(Long id,
                    String token,
                    LocalDateTime expiredDatetime,
                    LocalDateTime createDatetime) {
        this.id = id;
        this.token = token;
        this.expiredDatetime = expiredDatetime;
        this.createDatetime = createDatetime;
    }

    public static Waiting create(Long id,
                                 String token,
                                 LocalDateTime expiredDatetime,
                                 LocalDateTime createDatetime) {
        return new Waiting(id, token, expiredDatetime, createDatetime);
    }
}
