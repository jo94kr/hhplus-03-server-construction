package io.hhplus.server_construction.infra.entity;

import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "concert_seat")
public class ConcertSeatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schdule_id")
    private ConcertScheduleEntity concertSchedule;

    private String seatNum;

    private ConcertSeatEnums.Grade grade;

    private BigDecimal price;

    private ConcertSeatEnums.Status status;

    public ConcertSeatEntity(Long id,
                             ConcertScheduleEntity concertSchedule,
                             String seatNum,
                             ConcertSeatEnums.Grade grade,
                             BigDecimal price,
                             ConcertSeatEnums.Status status) {
        this.id = id;
        this.concertSchedule = concertSchedule;
        this.seatNum = seatNum;
        this.grade = grade;
        this.price = price;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcertSeatEntity that = (ConcertSeatEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
