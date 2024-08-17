package io.hhplus.server_construction.interfaces.consumer;

import io.hhplus.server_construction.IntegratedTest;
import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.repository.OutboxRepository;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.support.kafka.KafkaConstants;
import io.hhplus.server_construction.support.kafka.KafkaProducer;
import io.hhplus.server_construction.support.testcontainer.KafkaTestContainer;
import io.hhplus.server_construction.support.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class PaymentMessageConsumerTest extends IntegratedTest implements KafkaTestContainer {

    @Autowired
    WaitingRepository waitingRepository;

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    KafkaProducer kafkaProducer;

    @Test
    @DisplayName("메시지 발행후 토큰이 만료되는지 확인")
    void consumeTest() {
        // given
        String token = "DUMMY_TOKEN";
        waitingRepository.addActiveQueue(token);
        Boolean activeToken = waitingRepository.isActiveToken(token);
        assertThat(activeToken).isTrue();

        Outbox outbox = Outbox.init(MessageType.PAYMENT, JsonUtil.objectToString(token));
        outboxRepository.save(outbox);

        kafkaProducer.send(KafkaConstants.PAYMENT_TOPIC, outbox.getId(), token);

        // when
        // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(outboxRepository.findById(outbox.getId()).getStatus()).isEqualTo(OutboxStatus.PUBLISHED));
        Boolean result = waitingRepository.isActiveToken(token);
        assertThat(result).isFalse();
    }
}
