package app.hakai.backend.question.variants.dtos;

import org.kahai.framework.questions.variants.response.BaseQuestionVariantResponse;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

public class QuestionWithFeedbackVariantResponse extends BaseQuestionVariantResponse {
    private String feedback;

    public QuestionWithFeedbackVariantResponse(QuestionVariantResponse wrappee, String feedback) {
        super(wrappee);
        this.feedback = feedback;
    }
}
