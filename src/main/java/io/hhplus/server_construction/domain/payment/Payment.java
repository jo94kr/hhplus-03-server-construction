package io.hhplus.server_construction.domain.payment;

import io.hhplus.server_construction.domain.payment.exception.PaymentException;
import io.hhplus.server_construction.domain.payment.exception.PaymentExceptionEnums;
import io.hhplus.server_construction.domain.payment.vo.PaymentStatus;
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
    private final PaymentStatus status;
    private final LocalDateTime createDatetime;

    public Payment(Long id,
                   Reservation reservation,
                   User user,
                   BigDecimal price,
                   PaymentStatus status,
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
            throw new PaymentException(PaymentExceptionEnums.INVALID_USER);
        }

        return new Payment(null,
                reservation,
                user,
                reservation.getTotalPrice(),
                PaymentStatus.COMPLETE,
                null);
    }
}
