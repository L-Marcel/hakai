package app.hakai.backend.models;

import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.dtos.request.QuestionRequestBody;
import org.kahai.framework.models.questions.BaseQuestion;
import org.kahai.framework.models.questions.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@QuestionType("multipleChoice")
public class MultipleChoiceQuestion extends BaseQuestion implements QuestionRequestBody {
    public MultipleChoiceQuestion(Question wrappee) {
        super(wrappee);
    };

    @Override
    public String getPromptFormat() {
        // TODO - Conceito errado do promptFormat,
        // olhar os outros TODO e depois voltar aqui
        return "[Múltipla escolha] " + super.getPromptFormat();
    };

    @Override
    public Question toQuestion() {
        return this;
    };
};
