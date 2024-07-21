package io.hhplus.server_construction.domain.reservation.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus implements EnumInterface {
    PAYMENT_WAITING("PAYMENT_WAITING", "결제 대기"),
    PAYMENT_COMPLETE("PAYMENT_COMPLETE", "결제 완료"),
    CANCEL("CANCEL", "결제 취소");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<ReservationStatus> {
        @Override
        public ReservationStatus convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, ReservationStatus.class);
        }
    }

    public boolean isPaymentWaiting() {
        return this.equals(PAYMENT_WAITING);
    }
}
