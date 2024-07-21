package io.hhplus.server_construction.domain.payment.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus implements EnumInterface {

    RESERVATION("RESERVATION", "결제 대기"),
    COMPLETE("COMPLETE", "결제 완료"),
    CANCEL("CANCEL", "결제 취소");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<PaymentStatus> {
        @Override
        public PaymentStatus convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, PaymentStatus.class);
        }
    }
}
