package app.hakai.backend.question.variants;

import org.kahai.framework.questions.variants.BaseQuestionVariant;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.question.variants.dtos.QuestionWithFeedbackVariantResponse;

public class QuestionWithFeedbackVariant extends BaseQuestionVariant {
    private String feedback;

    @JsonCreator
    public QuestionWithFeedbackVariant(
        @JsonProperty("wrappee") QuestionVariant wrappee,
        @JsonProperty("feedback") String feedback
    ) {
        super(wrappee);
        this.feedback = feedback;
    }

    @Override
    public ConcreteQuestionVariant getRoot() {
        return wrappee.getRoot();
    }

    @Override
    public QuestionVariantResponse toResponse(Boolean hasAnswer) {
        return new QuestionWithFeedbackVariantResponse(
            this.getWrappee().toResponse(hasAnswer),
            this.feedback
        );
    }
    
}
