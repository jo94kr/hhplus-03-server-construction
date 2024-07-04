package io.hhplus.server_construction.domain.concert;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Concert {

    private final Long id;
    private final String name;
    private final LocalDateTime createDatetime;
    private final LocalDateTime modifyDatetime;

    private Concert(Long id,
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
