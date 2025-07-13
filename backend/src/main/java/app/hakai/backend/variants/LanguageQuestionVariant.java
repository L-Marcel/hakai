package app.hakai.backend.variants;

import java.util.List;

import org.kahai.framework.questions.variants.BaseQuestionVariant;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.dtos.LanguageQuestionVariantResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LanguageQuestionVariant extends BaseQuestionVariant {
    private String language;

    @JsonCreator
    public LanguageQuestionVariant(
            @JsonProperty("wrappee") QuestionVariant wrappee,
            @JsonProperty("language") String language) {
        super(wrappee);
        this.language = language;
    }

    @Override
    public ConcreteQuestionVariant getRoot() {
        return this.wrappee.getRoot();
    }

    @Override
    public QuestionVariantResponse toResponse(Boolean hasAnswer) {
        QuestionVariantResponse baseResponse = this.wrappee.toResponse(hasAnswer);
        return new LanguageQuestionVariantResponse(baseResponse, this.language);
    }
}