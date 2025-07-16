package app.hakai.backend.dtos;

import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.request.BaseQuestionRequest;
import org.kahai.framework.validation.ValidatorChain;

import app.hakai.backend.question.LanguageQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LanguageQuestionRequest extends BaseQuestionRequest {
    private String language;

    @Override
    public void validate(ValidatorChain validator, String prefix) {

        validator.validate(prefix + "language", this.language);

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

        return new LanguageQuestion(concreteQuestion, this.language);
    }
}