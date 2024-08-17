package io.hhplus.server_construction.infra.outbox.entity;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.infra.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "outbox")
public class OutboxEntity extends BaseEntity {

    @Id
    private String id;

    private MessageType messageType;

    @Column(columnDefinition = "longtext")
    private String message;

    private OutboxStatus status;

    private int cnt;

    public OutboxEntity(String id,
                        MessageType messageType,
                        String message,
                        OutboxStatus status,
                        int cnt) {
        this.id = id;
        this.messageType = messageType;
        this.message = message;
        this.status = status;
        this.cnt = cnt;
    }

    public Outbox toDomain() {
        return Outbox.builder()
                .id(this.id)
                .messageType(this.messageType)
                .message(this.message)
                .status(this.status)
                .cnt(this.cnt)
                .build();
    }

    public static OutboxEntity toEntity(Outbox outbox) {
        return new OutboxEntity(outbox.getId(),
                outbox.getMessageType(),
                outbox.getMessage(),
                outbox.getStatus(),
                outbox.getCnt());
    }
}
