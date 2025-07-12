package app.hakai.backend.question;

import java.util.List;
import org.kahai.framework.questions.BaseQuestion;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.hakai.backend.question.dtos.QuestionWithFeedbackResponse;
import app.hakai.backend.question.variants.QuestionWithFeedbackVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionWithFeedback extends BaseQuestion {
    private String feedback;

    @JsonCreator
    public QuestionWithFeedback(
        @JsonProperty("wrappee") Question wrappee,
        @JsonProperty("feedback") String feedback
    ) {
        super(wrappee);
        this.feedback = feedback;
    };

    @Override
    public List<Boolean> validate(List<String> candidates) {
        return this.wrappee.validate(candidates);
    };

    @Override
    public ConcreteQuestion getRoot() {
        return this.wrappee.getRoot();
    };

    @Override
    public QuestionResponse toResponse() {
        return new QuestionWithFeedbackResponse(
            this.getWrappee().toResponse(),
            this.getFeedback()
        );
    };

    @Override
    public Examples<? extends QuestionVariant> getPromptExamples() {
        return new Examples<>(
            new QuestionWithFeedbackVariant(
                this.wrappee.getPromptExamples().getFirst(),
                "A resposta correta é 35. A operação de adição consiste em combinar dois ou mais valores para encontrar um total. Para realizar a adição, somamos as unidades e depois as dezenas. As unidades são 0+5=5. As dezenas são 2+1=3. Neste caso, ao somar 20 e 15, o resultado obtido é 35."
            ), 
            new QuestionWithFeedbackVariant(
                this.wrappee.getPromptExamples().getSecond(),
                "A resposta correta é 2. A raiz quadrada de um número é o valor que, quando multiplicado por si mesmo (elevado ao quadrado), resulta no número original. No caso de 4, se você multiplicar 2 por 2 (2*2), o resultado é 4. É importante entender que a raiz quadrada é a operação inversa da potenciação ao quadrado."
            ),
            new QuestionWithFeedbackVariant(
                this.wrappee.getPromptExamples().getThird(),
                "A resposta correta é 5. Estamos procurando um número que, multiplicado por si mesmo, dê 25. Se você testar as opções ou pensar nos múltiplos, verá que 5*5=25. A raiz quadrada é um conceito fundamental, e números como 25, que são resultados de um número inteiro multiplicado por si mesmo, são chamados de quadrados perfeitos."
            )
        );
    };
    
};