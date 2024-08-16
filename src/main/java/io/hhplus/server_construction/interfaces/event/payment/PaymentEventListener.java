package io.hhplus.server_construction.interfaces.event.payment;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.service.OutboxService;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.server_construction.support.kafka.KafkaProducer;
import io.hhplus.server_construction.support.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static io.hhplus.server_construction.support.kafka.KafkaConstants.RESERVATION_TOPIC;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(PaymentSuccessEvent event) {
        outboxService.save(Outbox.init(MessageType.PAYMENT, JsonUtil.objectToString(event.getToken())));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tokenExpireHandler(PaymentSuccessEvent event) {
        kafkaProducer.send(RESERVATION_TOPIC, event.getOutboxId(), event.getToken());
    }
}
