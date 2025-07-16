package app.hakai.backend.question;

import java.util.List;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.dtos.LanguageQuestionResponse;
import app.hakai.backend.variants.LanguageQuestionVariant;

import org.kahai.framework.questions.BaseQuestion;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageQuestion extends BaseQuestion {
    private final String language;

    @JsonCreator
    public LanguageQuestion(
            @JsonProperty("wrappee") Question wrappee,
            @JsonProperty("language") String language) {
        super(wrappee);
        this.language = language;
    }

    @Override
    public QuestionResponse toResponse() {
        QuestionResponse originalResponse = this.wrappee.toResponse();

        return new LanguageQuestionResponse(originalResponse, this.language);
    }

    @Override
    public List<Boolean> validate(List<String> candidates) {
        return this.wrappee.validate(candidates);
    }

    @Override
    public ConcreteQuestion getRoot() {
        return this.wrappee.getRoot();
    }

    @Override
    public Examples<? extends QuestionVariant> getPromptExamples() {
        return new Examples<>(
            new LanguageQuestionVariant(
                this.wrappee.getPromptExamples().getFirst(),
                "Portugues"),
            new LanguageQuestionVariant(
                this.wrappee.getPromptExamples().getSecond(),
                "Inglês"),
            new LanguageQuestionVariant(
                this.wrappee.getPromptExamples().getThird(),
                "Espanhol")
        );
    }
}