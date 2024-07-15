package io.hhplus.server_construction.domain.concert;

import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ConcertSeat {

    private final Long id;
    private final ConcertSchedule concertSchedule;
    private final String seatNum;
    private final ConcertSeatGrade grade;
    private final BigDecimal price;
    private ConcertSeatStatus status;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    public ConcertSeat(Long id,
                       ConcertSchedule concertSchedule,
                       String seatNum,
                       ConcertSeatGrade grade,
                       BigDecimal price,
                       ConcertSeatStatus status,
                       LocalDateTime createDatetime,
                       LocalDateTime modifyDatetime) {
        this.id = id;
        this.concertSchedule = concertSchedule;
        this.seatNum = seatNum;
        this.grade = grade;
        this.price = price;
        this.status = status;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static ConcertSeat create(Long id,
                                     ConcertSchedule concertSchedule,
                                     String seatNum,
                                     ConcertSeatGrade grade,
                                     BigDecimal price,
                                     ConcertSeatStatus status,
                                     LocalDateTime createDatetime,
                                     LocalDateTime modifyDatetime) {
        return new ConcertSeat(id, concertSchedule, seatNum, grade, price, status, createDatetime, modifyDatetime);
    }

    public boolean isPossible() {
        return this.status.isPossible();
    }

    public ConcertSeat changeStatus(ConcertSeatStatus concertSeatStatus) {
        this.status = concertSeatStatus;
        return this;
    }
}
