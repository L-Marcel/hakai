package app.hakai.backend.dtos;

import org.kahai.framework.questions.response.BaseQuestionResponse;
import org.kahai.framework.questions.response.QuestionResponse;

public class LanguageQuestionResponse extends BaseQuestionResponse {

    private String language;

    public LanguageQuestionResponse(QuestionResponse wrappee, String language) {
        super(wrappee);
        this.language = language;

    }
}
