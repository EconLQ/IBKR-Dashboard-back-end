package com.liquidus.ibkrdasboardjee8.entity.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom deserializer to parse LocalDateTime time format from a POST/PUT request in {@link com.liquidus.ibkrdasboardjee8.rest.PositionEndpoint#updatePosition}
 * and {@link com.liquidus.ibkrdasboardjee8.rest.PositionEndpoint#createPosition(Position)}
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateString = parser.getText();
        return LocalDateTime.parse(dateString, formatter);
    }
}