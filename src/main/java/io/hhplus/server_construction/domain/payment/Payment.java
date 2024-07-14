package io.hhplus.server_construction.domain.payment;

import io.hhplus.server_construction.domain.payment.exception.InvalidUserException;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.user.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Payment {

    private final Long id;
    private final Reservation reservation;
    private final User user;
    private final BigDecimal price;
    private final PaymentEnums.PaymentStatus status;
    private final LocalDateTime createDatetime;

    public Payment(Long id,
                   Reservation reservation,
                   User user,
                   BigDecimal price,
                   PaymentEnums.PaymentStatus status,
                   LocalDateTime createDatetime) {
        this.id = id;
        this.reservation = reservation;
        this.user = user;
        this.price = price;
        this.status = status;
        this.createDatetime = createDatetime;
    }

    public static Payment pay(Reservation reservation,
                              User user) {
        // 동일한 주문자인지 체크
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new InvalidUserException();
        }

        return new Payment(null,
                reservation,
                user,
                reservation.getTotalPrice(),
                PaymentEnums.PaymentStatus.COMPLETE,
                null);
    }
}
