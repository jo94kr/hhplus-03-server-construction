package io.hhplus.server_construction.domain.outbox;

import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class Outbox {
    private final String id;
    private final MessageType messageType;
    private final String message;
    private OutboxStatus status;
    private int cnt;

    public Outbox(
            String id,
            MessageType messageType,
            String message,
            OutboxStatus status,
            int cnt
    ) {
        this.id = id;
        this.messageType = messageType;
        this.message = message;
        this.status = status;
        this.cnt = cnt;
    }

    public static Outbox init(MessageType messageType,
                              String message) {
        return Outbox.builder()
                .id(UUID.randomUUID().toString())
                .messageType(messageType)
                .message(message)
                .status(OutboxStatus.INIT)
                .cnt(0)
                .build();
    }

    public Outbox published() {
        this.status = OutboxStatus.PUBLISHED;
        return this;
    }

    public Outbox failed() {
        this.status = OutboxStatus.FAIL;
        return this;
    }

    public Outbox incrementCnt() {
        this.cnt = this.cnt + 1;
        return this;
    }
}
