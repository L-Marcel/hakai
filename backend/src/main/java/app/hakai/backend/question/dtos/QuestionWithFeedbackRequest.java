package app.hakai.backend.question.dtos;

import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.request.QuestionRequest;
import org.kahai.framework.validation.ValidatorChain;

public class QuestionWithFeedbackRequest implements QuestionRequest {

    @Override
    public void validate(ValidatorChain validator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public void validate(ValidatorChain validator, String prefix) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public Question toQuestion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toQuestion'");
    }
    
}
