package io.hhplus.server_construction.domain.outbox;

import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Outbox(
        String id,
        MessageType messageType,
        String message,
        OutboxStatus status,
        int cnt
) {

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
        return Outbox.builder()
                .id(this.id)
                .messageType(this.messageType)
                .message(this.message)
                .status(OutboxStatus.PUBLISHED)
                .cnt(this.cnt)
                .build();
    }

    public Outbox failed() {
        return Outbox.builder()
                .id(this.id)
                .messageType(this.messageType)
                .message(this.message)
                .status(OutboxStatus.FAIL)
                .cnt(this.cnt)
                .build();
    }

    public Outbox incrementCnt() {
        return Outbox.builder()
                .id(this.id)
                .messageType(this.messageType)
                .message(this.message)
                .status(this.status)
                .cnt(this.cnt + 1)
                .build();
    }
}
