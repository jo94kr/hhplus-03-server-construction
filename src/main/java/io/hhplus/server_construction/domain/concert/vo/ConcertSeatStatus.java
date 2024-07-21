package io.hhplus.server_construction.domain.concert.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcertSeatStatus implements EnumInterface {
    POSSIBLE("POSSIBLE", "예약 가능"),
    PENDING("PENDING", "임시 예약"),
    SOLD_OUT("SOLD_OUT", "매진");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<ConcertSeatStatus> {
        @Override
        public ConcertSeatStatus convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, ConcertSeatStatus.class);
        }
    }

    public boolean isPossible() {
        return this.equals(POSSIBLE);
    }
}
