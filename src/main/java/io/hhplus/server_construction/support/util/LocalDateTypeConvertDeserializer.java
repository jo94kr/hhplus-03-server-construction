package io.hhplus.server_construction.support.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Json => LocalDate 역직렬화
 */
public class LocalDateTypeConvertDeserializer extends StdDeserializer<LocalDate> {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    public LocalDateTypeConvertDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateString = jsonParser.getValueAsString();
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // 다음 패턴으로 계속 시도
            }
        }

        // 모든 패턴으로 파싱할 수 없으면 예외 던지기
        throw new RuntimeException("to localDate error");
    }
}
