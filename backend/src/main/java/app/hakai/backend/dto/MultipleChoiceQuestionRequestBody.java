package app.hakai.backend.dto;

import org.kahai.framework.dtos.request.BaseQuestionRequestBody;
import org.kahai.framework.models.questions.Question;

import app.hakai.backend.models.MultipleChoiceQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MultipleChoiceQuestionRequestBody extends BaseQuestionRequestBody {
    @Override
    public Question toQuestion() {
        Question base = super.toQuestion();
        return new MultipleChoiceQuestion(base);
    };
};
