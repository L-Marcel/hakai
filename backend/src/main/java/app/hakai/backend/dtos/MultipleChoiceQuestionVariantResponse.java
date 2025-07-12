package app.hakai.backend.dtos;

import java.util.List;

import org.kahai.framework.questions.variants.response.BaseQuestionVariantResponse;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import lombok.Getter;

@Getter
public class MultipleChoiceQuestionVariantResponse extends BaseQuestionVariantResponse {
    private final QuestionVariantResponse wrappee;

    public MultipleChoiceQuestionVariantResponse(QuestionVariantResponse wrappee) {
        super(wrappee);
        this.wrappee = wrappee;
    };
};