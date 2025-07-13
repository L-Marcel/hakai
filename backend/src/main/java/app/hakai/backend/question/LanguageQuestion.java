package app.hakai.backend.question;

import java.util.List;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.dtos.LanguageQuestionResponse;
import app.hakai.backend.dtos.MultipleChoiceQuestionResponse;
import app.hakai.backend.variants.LanguageQuestionVariant;
import app.hakai.backend.variants.MultipleChoiceQuestionVariant;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.BaseQuestion;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
                        new ConcreteQuestionVariant(
                                "Qual é a capital do Brasil?",
                                Difficulty.EASY,
                                List.of("Brasília", "Rio de Janeiro", "São Paulo")),
                        "Portugues"),

                new LanguageQuestionVariant(
                        new ConcreteQuestionVariant(
                                "What is the main ingredient in guacamole?",
                                Difficulty.NORMAL,
                                List.of("Avocado", "Tomato", "Onion")),
                        "Inglês"),

                new LanguageQuestionVariant(

                        new ConcreteQuestionVariant(
                                "¿Cuáles de los siguientes son colores primarios?",
                                Difficulty.HARD,
                                List.of("Rojo", "Azul", "Verde", "Amarillo")),
                        "Espanhol"));
    }
}