package io.hhplus.server_construction.infra.entity;

import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "waiting")
public class WaitingEntity extends BaseCreateDatetimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private WaitingStatus status;

    private LocalDateTime accessDatetime;

    private LocalDateTime expiredDatetime;

    public WaitingEntity(Long id,
                         String token,
                         WaitingStatus status,
                         LocalDateTime accessDatetime,
                         LocalDateTime expiredDatetime) {
        this.id = id;
        this.token = token;
        this.status = status;
        this.accessDatetime = accessDatetime;
        this.expiredDatetime = expiredDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitingEntity that = (WaitingEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
