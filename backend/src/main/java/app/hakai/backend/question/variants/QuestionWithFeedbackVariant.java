package app.hakai.backend.question.variants;

import java.util.List;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

public class QuestionWithFeedbackVariant implements QuestionVariant {

    public QuestionWithFeedbackVariant(String string, Difficulty difficulty, List<String> options, String feedback) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public ConcreteQuestionVariant getRoot() {
        return this.getRoot();
    }

    @Override
    public QuestionVariantResponse toResponse(Boolean hasAnswer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponse'");
    }
    
}
