package io.hhplus.server_construction.domain.concert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.hhplus.server_construction.support.serializer.LocalDateTimeDeserializer;
import io.hhplus.server_construction.support.serializer.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Concert implements Serializable {

    private Long id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDatetime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifyDatetime;

    public Concert(Long id,
                   String name,
                   LocalDateTime createDatetime,
                   LocalDateTime modifyDatetime) {
        this.id = id;
        this.name = name;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
    }

    public static Concert create(Long id,
                                 String name,
                                 LocalDateTime createDatetime,
                                 LocalDateTime modifyDatetime) {
        return new Concert(id, name, createDatetime, modifyDatetime);
    }
}
