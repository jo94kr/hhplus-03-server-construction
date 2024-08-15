package io.hhplus.server_construction.interfaces.kafka_test;

import io.hhplus.server_construction.IntegratedTest;
import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.repository.OutboxRepository;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.support.kafka.KafkaConstants;
import io.hhplus.server_construction.support.kafka.KafkaProducer;
import io.hhplus.server_construction.support.testcontainer.KafkaTestContainer;
import io.hhplus.server_construction.support.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
class KafkaTest extends IntegratedTest implements KafkaTestContainer {

    @Autowired
    private TestConsumer testConsumer;

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    @DisplayName("Kafka 연동 테스트")
    void kafkaTest() {
        // given
        int cnt = 5;
        String topic = "testTopic";
        for (int i = 0; i < cnt; i++) {
            testProducer.send(topic, "TEST MESSAGE! num:" + (i + 1));
        }

        // when
        // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(testConsumer.getTestPayloadList())
                        .hasSize(cnt));

        log.debug("testConsumer.getTestPayloadList(): {}", testConsumer.getTestPayloadList());
        log.debug("consume cnt: {}", testConsumer.getTestPayloadList().size());
    }

    @Test
    @DisplayName("consume 성공 - outbox 상태가 정상적으로 변경 됨")
    void consumeOutboxTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now);
        Reservation reservation = Reservation.create(1L, user, ReservationStatus.PAYMENT_WAITING, BigDecimal.ZERO);
        Outbox outbox = Outbox.init(MessageType.RESERVATION, JsonUtil.objectToString(reservation));
        outboxRepository.save(outbox);

        kafkaProducer.send(KafkaConstants.RESERVATION_TOPIC, outbox.getId(), reservation);

        // when
        // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(outboxRepository.findById(outbox.getId()).getStatus()).isEqualTo(OutboxStatus.PUBLISHED));
    }
}
