package app.hakai.backend.models;

import java.util.List;

import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.dtos.request.CreateQuestionRequestBody;
import org.kahai.framework.models.questions.BaseQuestion;
import org.kahai.framework.models.questions.Question;

@QuestionType("multipleChoice")
public class MultipleChoiceQuestion extends BaseQuestion implements CreateQuestionRequestBody {

    private List<String> options;

    public MultipleChoiceQuestion(Question wrappee, List<String> options) {
        super(wrappee);
        this.options = options;
    }

    @Override
    public String getPromptFormat() {
        return "[Múltipla escolha] " + super.getPromptFormat();
    }

    @Override
    public Question toQuestion() {
        return this;
    }
}
