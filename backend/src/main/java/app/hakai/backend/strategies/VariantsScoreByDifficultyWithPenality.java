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
        ConcreteQuestion rootQuestion = question.getRoot();
        Integer correctValue = rootQuestion.getCorrectValue();
        Boolean allIsCorrect = !corrects.contains(false);
        
        Integer scoreChange = 0;
        switch (participant.getCurrentDifficulty()) {
            case Difficulty.EASY:
                scoreChange = correctValue * 100;
                break;
            case Difficulty.NORMAL:
                scoreChange = correctValue * 200;
                break;
            case Difficulty.HARD:
                scoreChange = correctValue * 300;
                break;
            default:
                break;
        }
        
        if (allIsCorrect) {
            score += scoreChange;
        } else {
            score -= scoreChange;
        }

        return score;
    };
};