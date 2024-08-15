package io.hhplus.server_construction.domain.outbox.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType implements EnumInterface {

    RESERVATION("RESERVATION", "예약"),
    PAYMENT("PAYMENT", "결제");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<MessageType> {
        @Override
        public MessageType convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, MessageType.class);
        }
    }
}
