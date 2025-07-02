package app.hakai.backend.dto;

import java.util.List;

import org.kahai.framework.dtos.request.CreateBaseQuestionRequestBody;
import org.kahai.framework.dtos.request.CreateQuestionRequestBody;
import org.kahai.framework.models.questions.Question;

import app.hakai.backend.models.MultipleChoiceQuestion;

public class CreateMultipleChoiceQuestionRequestBody extends CreateBaseQuestionRequestBody {

    private List<String> options;

    public CreateMultipleChoiceQuestionRequestBody(CreateQuestionRequestBody wrappee, List<String> options) {
        super(wrappee);
        this.options = options;
    }

    @Override
    public Question toQuestion() {
        Question base = super.toQuestion();
        return new MultipleChoiceQuestion(base, options);
    }

    public List<String> getOptions() {
        return this.options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
