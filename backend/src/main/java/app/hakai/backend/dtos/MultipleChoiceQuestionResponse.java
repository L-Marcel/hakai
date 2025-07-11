package app.hakai.backend.dtos;

import java.util.List;

import org.kahai.framework.questions.response.BaseQuestionResponse;
import org.kahai.framework.questions.response.QuestionResponse;

import lombok.Getter;

@Getter
public class MultipleChoiceQuestionResponse extends BaseQuestionResponse {

    private final List<String> options;

    public MultipleChoiceQuestionResponse(QuestionResponse wrappee, List<String> options) {
        super(wrappee);
        this.options = options;
    }

}
