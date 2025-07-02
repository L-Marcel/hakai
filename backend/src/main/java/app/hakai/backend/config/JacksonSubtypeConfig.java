package app.hakai.backend.config;

import java.util.Set;

import java.util.List;
import java.util.Set;

import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.dtos.request.CreateQuestionRequestBody;
import org.kahai.framework.models.questions.BaseQuestion;
import org.kahai.framework.models.questions.Question;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import jakarta.annotation.PostConstruct;

@Configuration
public class JacksonSubtypeConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void registerSubtypes() {
        Reflections reflections = new Reflections("app.hakai");

        Set<Class<? extends CreateQuestionRequestBody>> subtypes
                = reflections.getSubTypesOf(CreateQuestionRequestBody.class);

        for (Class<?> subtype : subtypes) {
            QuestionType annotation = subtype.getAnnotation(QuestionType.class);
            if (annotation != null) {
                String typeName = annotation.value();
                objectMapper.registerSubtypes(new NamedType(subtype, typeName));
            }
        }
    }
}
