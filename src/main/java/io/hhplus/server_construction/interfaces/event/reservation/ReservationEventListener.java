package io.hhplus.server_construction.interfaces.event.reservation;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.service.OutboxService;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.reservation.event.ReservationInfoEvent;
import io.hhplus.server_construction.infra.kafka.KafkaProducer;
import io.hhplus.server_construction.support.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static io.hhplus.server_construction.infra.kafka.KafkaConstants.RESERVATION_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(ReservationInfoEvent event) {
        outboxService.save(Outbox.init(MessageType.RESERVATION, JsonUtil.objectToString(event.getReservation())));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationDataSendHandler(ReservationInfoEvent event) {
        kafkaProducer.send(RESERVATION_TOPIC, event.getOutboxId(), event.getReservation());
    }
}
