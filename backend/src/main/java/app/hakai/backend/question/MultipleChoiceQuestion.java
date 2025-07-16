package app.hakai.backend.question;

import java.util.List;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.dtos.MultipleChoiceQuestionResponse;
import app.hakai.backend.variants.MultipleChoiceQuestionVariant;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.BaseQuestion;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuestion extends BaseQuestion {

    @JsonCreator
    public MultipleChoiceQuestion(@JsonProperty("wrappee") Question wrappee) {
        super(wrappee);
    }

    @Override
    public QuestionResponse toResponse() {
        QuestionResponse originalResponse = this.wrappee.toResponse();

        return new MultipleChoiceQuestionResponse(originalResponse);
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
            new MultipleChoiceQuestionVariant(
                    this.wrappee.getPromptExamples().getFirst()),
            new MultipleChoiceQuestionVariant(
                    this.wrappee.getPromptExamples().getSecond()),
            new MultipleChoiceQuestionVariant(
                    this.wrappee.getPromptExamples().getThird())
        );
    }
}