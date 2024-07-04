package io.hhplus.server_construction.domain.waiting;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Participants {

    private final Long id;
    private final String token;
    private final LocalDateTime expiredDatetime;
    private final LocalDateTime createDatetime;

    private Participants(Long id,
                         String token,
                         LocalDateTime expiredDatetime,
                         LocalDateTime createDatetime) {
        this.id = id;
        this.token = token;
        this.expiredDatetime = expiredDatetime;
        this.createDatetime = createDatetime;
    }

    public static Participants create(Long id,
                                      String token,
                                      LocalDateTime expiredDatetime,
                                      LocalDateTime createDatetime) {
        return new Participants(id, token, expiredDatetime, createDatetime);
    }
}
