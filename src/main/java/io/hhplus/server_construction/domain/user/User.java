package io.hhplus.server_construction.domain.user;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class User {

    private final Long id;
    private final String name;
    private final BigDecimal amount;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    private User(Long id,
                 String name,
                 BigDecimal amount,
                 LocalDateTime createDatetime,
                 LocalDateTime modifyDatetime) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static User create(Long id,
                              String name,
                              BigDecimal amount,
                              LocalDateTime createDatetime,
                              LocalDateTime modifyDatetime) {
        return new User(id, name, amount, createDatetime, modifyDatetime);
    }
}
