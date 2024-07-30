package io.hhplus.server_construction.infra.concert.entity;

import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.infra.BaseEntity;
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
    @JoinColumn(name = "concert_schedule_id")
    private ConcertScheduleEntity concertSchedule;

    private String seatNum;

    private ConcertSeatGrade grade;

    private BigDecimal price;

    private ConcertSeatStatus status;

    public ConcertSeatEntity(Long id,
                             ConcertScheduleEntity concertSchedule,
                             String seatNum,
                             ConcertSeatGrade grade,
                             BigDecimal price,
                             ConcertSeatStatus status) {
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
