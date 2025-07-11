package app.hakai.backend.variants;

import java.util.List;

import org.kahai.framework.questions.variants.BaseQuestionVariant;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.dtos.MultipleChoiceQuestionVariantResponse;
import lombok.Getter;

@Getter
public class MultipleChoiceQuestionVariant extends BaseQuestionVariant {

    @JsonCreator
    public MultipleChoiceQuestionVariant(
            @JsonProperty("wrappee") QuestionVariant wrappee) {
        super(wrappee);
    }

    @Override
    public ConcreteQuestionVariant getRoot() {
        return this.wrappee.getRoot();
    }

    @Override
    public QuestionVariantResponse toResponse(Boolean hasAnswer) {
        QuestionVariantResponse baseResponse = this.wrappee.toResponse(hasAnswer);
        return new MultipleChoiceQuestionVariantResponse(baseResponse);
    }
}