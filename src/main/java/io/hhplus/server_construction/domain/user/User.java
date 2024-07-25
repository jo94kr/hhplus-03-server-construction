package io.hhplus.server_construction.domain.user;

import io.hhplus.server_construction.domain.user.exceprtion.UserException;
import io.hhplus.server_construction.domain.user.exceprtion.UserExceptionEnums;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class User {

    private final Long id;
    private final String name;
    private BigDecimal amount;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;
    private final Long version;

    private User(Long id,
                 String name,
                 BigDecimal amount,
                 LocalDateTime createDatetime,
                 LocalDateTime modifyDatetime,
                 Long version) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
        this.version = version;
    }

    public static User create(Long id,
                              String name,
                              BigDecimal amount,
                              LocalDateTime createDatetime,
                              LocalDateTime modifyDatetime,
                              Long version) {
        return new User(id, name, amount, createDatetime, modifyDatetime, version);
    }

    public User charge(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new UserException(UserExceptionEnums.INVALID_AMOUNT_VALUE);
        }

        this.amount = this.amount.add(amount);
        return this;
    }

    public User use(BigDecimal amount) {
        if (amount.compareTo(this.amount) < 0) {
            throw new UserException(UserExceptionEnums.INVALID_AMOUNT_VALUE);
        }
        BigDecimal subtracted = this.amount.subtract(amount);
        if (subtracted.compareTo(BigDecimal.ZERO) < 0) {
            throw new UserException(UserExceptionEnums.INVALID_AMOUNT_VALUE);
        }
        this.amount = subtracted;
        return this;
    }
}
