package app.hakai.backend.models;
import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.models.questions.BaseQuestion;
import org.kahai.framework.models.questions.Question;


@QuestionType("withFeedback")
public class QuestionWithFeedback extends BaseQuestion { 

    private String feedback; 

    public QuestionWithFeedback(Question question, String feedback) {
        super(question);
        this.feedback = feedback;
    }

    public String getFeedback() {
        return this.feedback;
    }
    @Override
    public String getPromptFormat() {
        return super.getPromptFormat() + "\n[Feedback Adicional]: " + this.feedback;
    }
}