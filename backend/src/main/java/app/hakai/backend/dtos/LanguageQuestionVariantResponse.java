package app.hakai.backend.dtos;

import java.util.List;

import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import lombok.Getter;

@Getter
public class LanguageQuestionVariantResponse implements QuestionVariantResponse {

    private final QuestionVariantResponse wrappee;
    private String language;

    public LanguageQuestionVariantResponse(QuestionVariantResponse wrappee, String language) {
        this.wrappee = wrappee;
        this.language = language;

    }
}