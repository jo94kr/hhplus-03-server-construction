package io.hhplus.server_construction.domain.payment;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationEnums;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Payment {

    private final Long id;
    private final Reservation reservation;
    private final BigDecimal price;
    private final PaymentEnums.PaymentStatus status;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    private Payment(Long id,
                    Reservation reservation,
                    BigDecimal price,
                    PaymentEnums.PaymentStatus status,
                    LocalDateTime createDatetime,
                    LocalDateTime modifyDatetime) {
        this.id = id;
        this.reservation = reservation;
        this.price = price;
        this.status = status;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static Payment create(Long id,
                                 Reservation reservation,
                                 BigDecimal price,
                                 PaymentEnums.PaymentStatus status,
                                 LocalDateTime createDatetime,
                                 LocalDateTime modifyDatetime) {
        return new Payment(id, reservation, price, status, createDatetime, modifyDatetime);
    }
}
