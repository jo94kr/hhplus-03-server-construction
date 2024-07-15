package io.hhplus.server_construction.common.enums;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public interface EnumConverter<T extends EnumInterface> extends AttributeConverter<T, String> {

    @Override
    default String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    default T convertToEntityAttribute(String dbData) {
        return null;
    }

    default T convertToEntityAttribute(String dbData, Class<T> tClass) {
        return Arrays.stream(tClass.getEnumConstants())
                .filter(e -> e.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + dbData));
    }
}
