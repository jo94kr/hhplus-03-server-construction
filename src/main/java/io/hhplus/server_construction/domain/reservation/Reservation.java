package io.hhplus.server_construction.domain.reservation;

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
    private List<ReservationItem> reservationItemList;
    private LocalDateTime createDatetime;

    private Reservation(Long id,
                        User user,
                        BigDecimal totalPrice,
                        List<ReservationItem> reservationItemList,
                        LocalDateTime createDatetime) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.reservationItemList = reservationItemList;
        this.createDatetime = createDatetime;
    }

    private Reservation(Long id,
                        User user,
                        BigDecimal totalPrice) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
    }

    public static Reservation create(Long id,
                                     User user,
                                     BigDecimal totalPrice,
                                     List<ReservationItem> reservationItemList,
                                     LocalDateTime createDatetime) {
        return new Reservation(id, user, totalPrice, reservationItemList, createDatetime);
    }

    public static Reservation create(Long id,
                                     User user,
                                     BigDecimal totalPrice) {
        return new Reservation(id, user, totalPrice);
    }

    public Reservation setReservationItemList(List<ReservationItem> reservationItemList) {
        this.reservationItemList = reservationItemList;
        return this;
    }
}
