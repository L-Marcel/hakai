package app.hakai.backend.dtos;

import java.util.List;

import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.request.BaseQuestionRequest;
import org.kahai.framework.validation.ValidatorChain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageQuestionRequest extends BaseQuestionRequest {

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