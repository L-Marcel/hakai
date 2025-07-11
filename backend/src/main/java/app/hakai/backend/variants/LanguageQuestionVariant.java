package app.hakai.backend.variants;

import java.util.List;

import org.kahai.framework.questions.variants.BaseQuestionVariant;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import app.hakai.backend.dtos.LanguageQuestionVariantResponse;
import lombok.Getter;

@Getter
public class LanguageQuestionVariant extends BaseQuestionVariant {
    private String language;

    public LanguageQuestionVariant(QuestionVariant wrappee, String language) {
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