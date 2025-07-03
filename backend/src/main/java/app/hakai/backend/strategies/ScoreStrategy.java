package app.hakai.backend.strategies;

import java.util.List;

import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.services.strategies.VariantsScoreStrategy;
import org.kahai.framework.transients.Participant;
import org.springframework.stereotype.Component;

@Component
public class ScoreStrategy implements VariantsScoreStrategy {

    @Override
    public int calculate(Participant participant, Question question, List<Boolean> corrects) {
        boolean isCorrect = corrects.stream().anyMatch(c -> c);

        if (isCorrect) {
            ConcreteQuestion rootQuestion = question.getRoot();
            return rootQuestion.getCorrectValue() != null ? rootQuestion.getCorrectValue() : 0;
        }

        return 0;
    }
}