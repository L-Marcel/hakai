package app.hakai.backend.dtos;

import java.util.List;

import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import lombok.Getter;

@Getter
public class MultipleChoiceQuestionVariantResponse implements QuestionVariantResponse {

    private final QuestionVariantResponse wrappee;
    private final List<String> options;

    public MultipleChoiceQuestionVariantResponse(QuestionVariantResponse wrappee, List<String> options) {
        this.wrappee = wrappee;
        this.options = options;
    }
}