package app.hakai.backend.strategies;

import java.util.List;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.services.strategies.VariantsScoreStrategy;
import org.kahai.framework.transients.Participant;
import org.springframework.stereotype.Component;

@Component
public class VariantsScoreByDifficultyWithPenality implements VariantsScoreStrategy {
    @Override
    public Integer calculate(Participant participant, Question question, List<Boolean> corrects) {
        Integer score = participant.getScore();
        boolean isCorrect = corrects.get(0);
        
        Integer scoreChange = 0;
        switch (participant.getCurrentDifficulty()) {
            case Difficulty.EASY:
                scoreChange = 100;
                break;
            case Difficulty.NORMAL:
                scoreChange = 200;
                break;
            case Difficulty.HARD:
                scoreChange = 300;
                break;
            default:
                break;
        }
        
        if (isCorrect) {
            score += scoreChange;
        } else {
            score -= scoreChange;
        }

        return score;
    };
};