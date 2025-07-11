package app.hakai.backend.question;

import java.util.List;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

import app.hakai.backend.dtos.MultipleChoiceQuestionResponse;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.BaseQuestion;
import org.kahai.framework.questions.ConcreteQuestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuestion extends BaseQuestion {
    private final List<String> options;

    public MultipleChoiceQuestion(Question wrappee, List<String> options) {
        super(wrappee);
        this.options = options;
    }

    @Override
    public QuestionResponse toResponse() {
        QuestionResponse originalResponse = this.wrappee.toResponse();

        return new MultipleChoiceQuestionResponse(originalResponse, this.options);
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
                new ConcreteQuestionVariant(
                        "Marque todos os animais da lista que são mamíferos.",
                        Difficulty.EASY,
                        List.of("Cachorro", "Gato", "Pássaro")),
                new ConcreteQuestionVariant(
                        "Selecione todos os países listados que são membros da União Europeia.",
                        Difficulty.NORMAL,
                        List.of("Alemanha", "França", "Brasil", "Japão", "Itália")),
                new ConcreteQuestionVariant(
                        "Dentre os elementos a seguir, indique todos que pertencem ao grupo dos gases nobres.",
                        Difficulty.HARD,
                        List.of("Hélio", "Neônio", "Oxigênio", "Hidrogênio", "Carbono", "Argônio")));
    }
}