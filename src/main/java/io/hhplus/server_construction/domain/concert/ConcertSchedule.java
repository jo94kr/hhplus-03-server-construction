package io.hhplus.server_construction.domain.concert;

import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleEnums;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConcertSchedule {

    private final Long id;
    private final Concert concert;
    private final LocalDateTime concertDatetime;
    private final ConcertScheduleEnums.ScheduleStatus status;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    public ConcertSchedule(Long id,
                            Concert concert,
                            LocalDateTime concertDatetime,
                            ConcertScheduleEnums.ScheduleStatus status,
                            LocalDateTime createDatetime,
                            LocalDateTime modifyDatetime) {
        this.id = id;
        this.concert = concert;
        this.concertDatetime = concertDatetime;
        this.status = status;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static ConcertSchedule create(Long id,
                                         Concert concert,
                                         LocalDateTime concertDatetime,
                                         ConcertScheduleEnums.ScheduleStatus status,
                                         LocalDateTime createDatetime,
                                         LocalDateTime modifyDatetime) {
        return new ConcertSchedule(id,
                concert,
                concertDatetime,
                status,
                createDatetime,
                modifyDatetime);
    }
}
