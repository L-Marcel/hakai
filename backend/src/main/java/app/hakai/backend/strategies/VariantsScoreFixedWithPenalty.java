package app.hakai.backend.strategies;

import java.util.List;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.services.strategies.VariantsScoreStrategy;
import org.kahai.framework.transients.Participant;
import org.springframework.stereotype.Component;

@Component
public class VariantsScoreFixedWithPenalty implements VariantsScoreStrategy {
    @Override
    public Integer calculate(Participant participant, Question question, List<Boolean> corrects) {

        ConcreteQuestion rootQuestion = question.getRoot();

        int correctValue = rootQuestion.getCorrectValue();
        int wrongValue = rootQuestion.getWrongValue();

        int totalScore = 0;
        for (Boolean isCorrect : corrects) {
            if (isCorrect) {
                totalScore += correctValue;
            } else {
                if (wrongValue < 0) {
                    totalScore += wrongValue;
                } else {
                    totalScore -= wrongValue;
                }
            }
        }
        return totalScore;
    }
}
