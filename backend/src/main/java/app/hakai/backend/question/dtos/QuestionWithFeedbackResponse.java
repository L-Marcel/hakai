package app.hakai.backend.question.dtos;

import org.kahai.framework.questions.response.BaseQuestionResponse;
import org.kahai.framework.questions.response.QuestionResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionWithFeedbackResponse extends BaseQuestionResponse {
    private String feedback;

    public QuestionWithFeedbackResponse(QuestionResponse wrappee, String feedback) {
        super(wrappee);
        this.feedback = feedback;
    }
}
