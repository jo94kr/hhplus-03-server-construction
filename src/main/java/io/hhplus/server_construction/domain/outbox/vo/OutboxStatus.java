package io.hhplus.server_construction.domain.outbox.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutboxStatus implements EnumInterface {

    INIT("INIT", "처리전"),
    PUBLISHED("PUBLISHED", "발행완료"),
    FAIL("FAIL", "실패");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<OutboxStatus> {
        @Override
        public OutboxStatus convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, OutboxStatus.class);
        }
    }
}
