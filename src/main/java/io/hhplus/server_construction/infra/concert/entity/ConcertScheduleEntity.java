package io.hhplus.server_construction.infra.concert.entity;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleEnums;
import io.hhplus.server_construction.infra.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "concert_schedule")
public class ConcertScheduleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private ConcertEntity concert;

    private LocalDateTime concertDatetime;

    private ConcertScheduleEnums.ScheduleStatus status;

    public ConcertScheduleEntity(Long id,
                                 ConcertEntity concert,
                                 LocalDateTime concertDatetime,
                                 ConcertScheduleEnums.ScheduleStatus status) {
        this.id = id;
        this.concert = concert;
        this.concertDatetime = concertDatetime;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcertScheduleEntity that = (ConcertScheduleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
