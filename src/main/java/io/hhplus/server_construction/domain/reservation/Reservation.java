package io.hhplus.server_construction.domain.reservation;

import io.hhplus.server_construction.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Reservation {

    private final Long id;
    private final User user;
    private final LocalDateTime createDatetime;

    private Reservation(Long id, User user, LocalDateTime createDatetime) {
        this.id = id;
        this.user = user;
        this.createDatetime = createDatetime;
    }

    public static Reservation create(Long id, User user, LocalDateTime createDatetime) {
        return new Reservation(id, user, createDatetime);
    }
}
