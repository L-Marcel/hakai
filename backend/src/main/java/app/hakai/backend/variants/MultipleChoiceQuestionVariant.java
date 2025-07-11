package app.hakai.backend.variants;

import java.util.List;

import org.kahai.framework.questions.variants.BaseQuestionVariant;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import app.hakai.backend.dtos.MultipleChoiceQuestionVariantResponse;
import lombok.Getter;

@Getter
public class MultipleChoiceQuestionVariant extends BaseQuestionVariant {

    private final List<String> options;

    public MultipleChoiceQuestionVariant(QuestionVariant wrappee, List<String> options) {
        super(wrappee);
        this.options = options;
    }

    @Override
    public ConcreteQuestionVariant getRoot() {
        return this.wrappee.getRoot();
    }

    @Override
    public QuestionVariantResponse toResponse(Boolean hasAnswer) {
        QuestionVariantResponse baseResponse = this.wrappee.toResponse(hasAnswer);
        return new MultipleChoiceQuestionVariantResponse(baseResponse, this.options);
    }
}