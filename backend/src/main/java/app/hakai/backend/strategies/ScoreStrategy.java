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
        int score = 0;
        ConcreteQuestion rootQuestion = question.getRoot();
        Integer correctValue = rootQuestion.getCorrectValue();
        Integer wrongValue = rootQuestion.getWrongValue();

        for (Boolean isCorrect : corrects) {
            if (isCorrect) {
                score += (correctValue != null ? correctValue : 0);
            } else {
                score += (wrongValue != null ? wrongValue : 0);
            };
        };

        return score;
    };
};