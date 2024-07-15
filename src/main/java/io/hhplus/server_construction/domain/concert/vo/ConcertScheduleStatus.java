package io.hhplus.server_construction.domain.concert.vo;

import io.hhplus.server_construction.common.enums.EnumConverter;
import io.hhplus.server_construction.common.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcertScheduleStatus implements EnumInterface {
    SOLD_OUT("SOLD_OUT", "매진"),
    AVAILABLE("AVAILABLE", "예약 가능");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<ConcertScheduleStatus> {
        @Override
        public ConcertScheduleStatus convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, ConcertScheduleStatus.class);
        }
    }

    public boolean isAvailable() {
        return this.equals(AVAILABLE);
    }
}
