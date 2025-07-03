package app.hakai.backend.config;

import java.util.Set;

import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.config.JacksonSubtypeConfigurer;
import org.kahai.framework.dtos.request.QuestionRequestBody;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

@Component
public class JacksonSubtypeConfig implements JacksonSubtypeConfigurer {

    @Override
    public void configure(ObjectMapper objectMapper) {
        Reflections reflections = new Reflections("app.hakai");

        Set<Class<? extends QuestionRequestBody>> subtypes = reflections.getSubTypesOf(QuestionRequestBody.class);

        for (Class<?> subtype : subtypes) {
            QuestionType annotation = subtype.getAnnotation(QuestionType.class);
            // Garante que a anotação existe e tem um valor antes de registrar
            if (annotation != null && !annotation.value().isBlank()) {
                String typeName = annotation.value();
                objectMapper.registerSubtypes(new NamedType(subtype, typeName));
            }
        }
    }
}