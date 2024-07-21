package io.hhplus.server_construction.domain.waiting.vo;

import io.hhplus.server_construction.support.enums.EnumConverter;
import io.hhplus.server_construction.support.enums.EnumInterface;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WaitingStatus implements EnumInterface {
    WAITING("WAITING", "대기중"),
    PROCEEDING("PROCEEDING", "진행중"),
    EXPIRED("EXPIRED", "만료");

    private final String code;
    private final String codeName;

    @Converter(autoApply = true)
    public static class JpaConverter implements EnumConverter<WaitingStatus> {
        @Override
        public WaitingStatus convertToEntityAttribute(String dbData) {
            return EnumConverter.super.convertToEntityAttribute(dbData, WaitingStatus.class);
        }
    }
}
