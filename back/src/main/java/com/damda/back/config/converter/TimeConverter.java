package com.damda.back.config.converter;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class TimeConverter implements AttributeConverter<LocalDateTime, String> {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd-HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    @Override
    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(FORMATTER);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String formattedDateTime) {
        if (formattedDateTime == null) {
            return null;
        }
        return LocalDateTime.parse(formattedDateTime, FORMATTER);
    }


}
