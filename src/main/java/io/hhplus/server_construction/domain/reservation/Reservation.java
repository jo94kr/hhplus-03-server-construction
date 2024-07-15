package io.hhplus.server_construction.domain.reservation;

import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Reservation {

    private final Long id;
    private final User user;
    private final BigDecimal totalPrice;
    private ReservationStatus status;
    private List<ReservationItem> reservationItemList;
    private LocalDateTime createDatetime;

    private Reservation(Long id,
                        User user,
                        BigDecimal totalPrice,
                        ReservationStatus status,
                        List<ReservationItem> reservationItemList,
                        LocalDateTime createDatetime) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.reservationItemList = reservationItemList;
        this.createDatetime = createDatetime;
    }

    private Reservation(Long id,
                        User user,
                        ReservationStatus status,
                        BigDecimal totalPrice) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public static Reservation create(Long id,
                                     User user,
                                     BigDecimal totalPrice,
                                     ReservationStatus status,
                                     List<ReservationItem> reservationItemList,
                                     LocalDateTime createDatetime) {
        return new Reservation(id, user, totalPrice, status, reservationItemList, createDatetime);
    }

    public static Reservation create(Long id,
                                     User user,
                                     ReservationStatus status,
                                     BigDecimal totalPrice) {
        return new Reservation(id, user, status, totalPrice);
    }

    public Reservation setReservationItemList(List<ReservationItem> reservationItemList) {
        this.reservationItemList = reservationItemList;
        return this;
    }

    public boolean isPaymentWaiting() {
        return this.status.isPaymentWaiting();
    }

    public Reservation changeStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }
}
