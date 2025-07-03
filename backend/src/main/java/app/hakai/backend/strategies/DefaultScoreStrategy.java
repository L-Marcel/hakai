package app.hakai.backend.strategies;

import java.util.List;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.services.strategies.VariantsScoreStrategy;
import org.kahai.framework.transients.Participant;
import org.springframework.stereotype.Component;

@Component
public class DefaultScoreStrategy implements VariantsScoreStrategy {

    @Override
    public int calculate(Participant participant, Question question, List<Boolean> corrects) {
        ConcreteQuestion concreteQuestion = question.getRoot();
        int score = 0;

        for (Boolean isCorrect : corrects) {
            if (isCorrect) {
                score += concreteQuestion.getCorrectValue();
            } else {
                score += concreteQuestion.getWrongValue();
            }
        }

        return score;
    }
}