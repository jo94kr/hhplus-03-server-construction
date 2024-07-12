package io.hhplus.server_construction.infra.entity;

import io.hhplus.server_construction.domain.payment.PaymentEnums;
import io.hhplus.server_construction.domain.reservation.Reservation;
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
@Table(name = "payment")
public class PaymentEntity extends BaseCreateDatetimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private BigDecimal price;

    private PaymentEnums.PaymentStatus status;

    private LocalDateTime paymentDateTime;

    public PaymentEntity(Long id,
                         ReservationEntity reservation,
                         UserEntity user,
                         BigDecimal price,
                         PaymentEnums.PaymentStatus status,
                         LocalDateTime paymentDateTime) {
        this.id = id;
        this.reservation = reservation;
        this.user = user;
        this.price = price;
        this.status = status;
        this.paymentDateTime = paymentDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
