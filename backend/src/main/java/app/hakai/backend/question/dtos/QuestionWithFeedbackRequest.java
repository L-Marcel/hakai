package app.hakai.backend.question.dtos;

import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.request.BaseQuestionRequest;
import org.kahai.framework.validation.ValidatorChain;

import app.hakai.backend.question.QuestionWithFeedback;

public class QuestionWithFeedbackRequest extends BaseQuestionRequest {
    private String feedback;

    @Override
    public void validate(ValidatorChain validator, String prefix) {        
        validator.validate(prefix + "feedback", this.feedback);

        if (this.wrappee != null) {
            this.wrappee.validate(validator, prefix);
        }
    }

    @Override
    public void validate(ValidatorChain validator) {
        this.validate(validator, "");
    }

    @Override
    public Question toQuestion() {
        Question concreteQuestion = this.wrappee.toQuestion();

        return new QuestionWithFeedback(concreteQuestion, this.feedback);
    }
    
}
