package io.hhplus.server_construction.domain.concert;

import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ConcertSeat {

    private final Long id;
    private final ConcertSchedule concertSchedule;
    private final String seatNum;
    private final ConcertSeatEnums.Grade grade;
    private final BigDecimal price;
    private ConcertSeatEnums.Status status;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    public ConcertSeat(Long id,
                       ConcertSchedule concertSchedule,
                       String seatNum,
                       ConcertSeatEnums.Grade grade,
                       BigDecimal price,
                       ConcertSeatEnums.Status status,
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
                                     ConcertSeatEnums.Grade grade,
                                     BigDecimal price,
                                     ConcertSeatEnums.Status status,
                                     LocalDateTime createDatetime,
                                     LocalDateTime modifyDatetime) {
        return new ConcertSeat(id, concertSchedule, seatNum, grade, price, status, createDatetime, modifyDatetime);
    }

    public boolean isPossible() {
        return this.status.isPossible();
    }

    public ConcertSeat changeStatus(ConcertSeatEnums.Status possible) {
        this.status = ConcertSeatEnums.Status.PENDING;
        return this;
    }
}
