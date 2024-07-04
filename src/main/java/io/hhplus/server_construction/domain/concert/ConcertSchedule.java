package io.hhplus.server_construction.domain.concert;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConcertSchedule {

    private final Long id;
    private final Concert concert;
    private final LocalDateTime concertDatetime;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    private ConcertSchedule(Long id,
                            Concert concert,
                            LocalDateTime concertDatetime,
                            LocalDateTime createDatetime,
                            LocalDateTime modifyDatetime) {
        this.id = id;
        this.concert = concert;
        this.concertDatetime = concertDatetime;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static ConcertSchedule create(Long id,
                                         Concert concert,
                                         LocalDateTime concertDatetime,
                                         LocalDateTime createDatetime,
                                         LocalDateTime modifyDatetime) {
        return new ConcertSchedule(id, concert, concertDatetime, createDatetime, modifyDatetime);
    }
}
