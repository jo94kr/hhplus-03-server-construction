package io.hhplus.server_construction.domain.concert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.hhplus.server_construction.support.serializer.LocalDateTimeDeserializer;
import io.hhplus.server_construction.support.serializer.LocalDateTimeSerializer;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ConcertSchedule implements Serializable {

    private final Long id;

    private final Concert concert;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime concertDatetime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime createDatetime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime modifyDatetime;

    public ConcertSchedule(Long id,
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
        return new ConcertSchedule(id,
                concert,
                concertDatetime,
                createDatetime,
                modifyDatetime);
    }
}
