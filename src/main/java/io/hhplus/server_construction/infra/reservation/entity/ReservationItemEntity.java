package io.hhplus.server_construction.infra.reservation.entity;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.reservation.ReservationEnums;
import io.hhplus.server_construction.infra.BaseCreateDatetimeEntity;
import io.hhplus.server_construction.infra.concert.entity.ConcertSeatEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reservation_item")
public class ReservationItemEntity extends BaseCreateDatetimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_seat_id")
    private ConcertSeatEntity concertSeat;

    private BigDecimal price;

    private ReservationEnums.ReservationStatus status;

    public ReservationItemEntity(Long id,
                                 ReservationEntity reservation,
                                 ConcertSeatEntity concertSeat,
                                 BigDecimal price,
                                 ReservationEnums.ReservationStatus status) {
        this.id = id;
        this.reservation = reservation;
        this.concertSeat = concertSeat;
        this.price = price;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationItemEntity that = (ReservationItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
