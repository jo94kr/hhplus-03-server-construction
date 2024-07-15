package io.hhplus.server_construction.domain.reservation;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ReservationItem {

    private final Long id;
    private final Reservation reservation;
    private final ConcertSeat concertSeat;
    private final BigDecimal price;
    private LocalDateTime createDatetime;

    private ReservationItem(Long id,
                            Reservation reservation,
                            ConcertSeat concertSeat,
                            BigDecimal price,
                            LocalDateTime createDatetime) {
        this.id = id;
        this.reservation = reservation;
        this.concertSeat = concertSeat;
        this.price = price;
        this.createDatetime = createDatetime;
    }

    private ReservationItem(Long id,
                            Reservation reservation,
                            ConcertSeat concertSeat,
                            BigDecimal price) {
        this.id = id;
        this.reservation = reservation;
        this.concertSeat = concertSeat;
        this.price = price;
    }

    public static ReservationItem create(Long id,
                                         Reservation reservation,
                                         ConcertSeat concertSeat,
                                         BigDecimal price,
                                         LocalDateTime createDatetime) {
        return new ReservationItem(id, reservation, concertSeat, price, createDatetime);
    }

    public static ReservationItem reservation(Reservation reservation,
                                              ConcertSeat concertSeat) {
        return new ReservationItem(null,
                reservation,
                concertSeat,
                concertSeat.getPrice());
    }
}
