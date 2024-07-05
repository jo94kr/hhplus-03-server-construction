package io.hhplus.server_construction.domain.concert;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ConcertSeat {

    private final Long id;
    private final ConcertSchedule concertSchedule;
    private final String seatNum;
    private final ConcertEnums.Grade grade;
    private final BigDecimal price;
    private final ConcertEnums.Status status;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    private ConcertSeat(Long id,
                        ConcertSchedule concertSchedule,
                        String seatNum,
                        ConcertEnums.Grade grade,
                        BigDecimal price,
                        ConcertEnums.Status status,
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
                                     ConcertEnums.Grade grade,
                                     BigDecimal price,
                                     ConcertEnums.Status status,
                                     LocalDateTime createDatetime,
                                     LocalDateTime modifyDatetime) {
        return new ConcertSeat(id, concertSchedule, seatNum, grade, price, status, createDatetime, modifyDatetime);
    }
}