package app.hakai.backend.config;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;

public class StringToUuidConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(String source){
        try {
            return UUID.fromString(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
