package app.hakai.backend.dtos;

import java.util.List;

import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.request.BaseQuestionRequest;
import org.kahai.framework.questions.request.ConcreteQuestionRequest;
import org.kahai.framework.validation.ValidatorChain;

import app.hakai.backend.question.MultipleChoiceQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MultipleChoiceQuestionRequest extends BaseQuestionRequest {

    public MultipleChoiceQuestionRequest(ConcreteQuestionRequest wrappee) {
        super(wrappee);
    }

    @Override
    public void validate(ValidatorChain validator, String prefix) {
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
        return new MultipleChoiceQuestion(concreteQuestion);
    }
}
