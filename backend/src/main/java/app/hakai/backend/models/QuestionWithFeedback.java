package app.hakai.backend.models;
import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.models.questions.BaseQuestion;
import org.kahai.framework.models.questions.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@QuestionType("withFeedback")
public class QuestionWithFeedback extends BaseQuestion { 
    private String feedback; 
    
    public QuestionWithFeedback(Question question, String feedback) {
        super(question);
        this.feedback = feedback;
    };

    @Override
    public String getPromptFormat() {
        // TODO - Conceito errado do promptFormat,
        // olhar os outros TODO e depois voltar aqui
        return super.getPromptFormat() + "\n[Feedback Adicional]: " + this.feedback;
    };
};