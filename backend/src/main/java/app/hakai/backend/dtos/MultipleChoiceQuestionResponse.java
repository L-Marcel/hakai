package app.hakai.backend.dtos;

import org.kahai.framework.questions.response.BaseQuestionResponse;
import org.kahai.framework.questions.response.QuestionResponse;

import lombok.Getter;

@Getter
public class MultipleChoiceQuestionResponse extends BaseQuestionResponse {

    public MultipleChoiceQuestionResponse(QuestionResponse wrappee) {
        super(wrappee);
    }

}
