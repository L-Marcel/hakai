package app.hakai.backend.dtos;

import org.kahai.framework.questions.variants.response.BaseQuestionVariantResponse;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import lombok.Getter;

@Getter
public class LanguageQuestionVariantResponse extends BaseQuestionVariantResponse {

    private String language;

    public LanguageQuestionVariantResponse(QuestionVariantResponse wrappee, String language) {
        super(wrappee);
        this.language = language;
    }
}