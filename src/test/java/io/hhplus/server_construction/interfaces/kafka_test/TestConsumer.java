package io.hhplus.server_construction.interfaces.kafka_test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class TestConsumer {

    private final List<String> testPayloadList = new ArrayList<>();

    @KafkaListener(topics = "testTopic", groupId = "testGroup")
    public void consume(String payload) {
        log.debug("payload: {}", payload);

        // 테스트용 데이터 저장
        testPayloadList.add(payload);
    }
}
