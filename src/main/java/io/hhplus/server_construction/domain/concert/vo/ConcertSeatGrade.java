package io.hhplus.server_construction.domain.concert.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcertSeatGrade implements EnumInterface {
    GOLD("GOLD","gold 등급"),
    SILVER("SILVER","silver 등급"),
    BRONZE("BRONZE","Bronze 등급");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<ConcertSeatGrade> {
        @Override
        public ConcertSeatGrade convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, ConcertSeatGrade.class);
        }
    }
}
