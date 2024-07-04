package io.hhplus.server_construction.domain.reservation;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ReservationPayment {

    private final Long id;
    private final Reservation reservation;
    private final BigDecimal price;
    private final ReservationEnums.PaymentStatus status;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    private ReservationPayment(Long id,
                               Reservation reservation,
                               BigDecimal price,
                               ReservationEnums.PaymentStatus status,
                               LocalDateTime createDatetime,
                               LocalDateTime modifyDatetime) {
        this.id = id;
        this.reservation = reservation;
        this.price = price;
        this.status = status;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static ReservationPayment create(Long id,
                                            Reservation reservation,
                                            BigDecimal price,
                                            ReservationEnums.PaymentStatus status,
                                            LocalDateTime createDatetime,
                                            LocalDateTime modifyDatetime) {
        return new ReservationPayment(id, reservation, price, status, createDatetime, modifyDatetime);
    }
}
