package io.hhplus.server_construction.interfaces.consumer;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class ReservationMessageConsumerTest extends IntegratedTest implements KafkaTestContainer {

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    KafkaProducer kafkaProducer;

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
