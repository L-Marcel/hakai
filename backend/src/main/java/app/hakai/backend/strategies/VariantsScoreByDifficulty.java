package app.hakai.backend.strategies;

import java.util.List;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.services.strategies.VariantsScoreStrategy;
import org.kahai.framework.transients.Participant;
import org.springframework.stereotype.Component;

@Component
public class VariantsScoreByDifficulty implements VariantsScoreStrategy {
    @Override
    public Integer calculate(Participant participant, Question question, List<Boolean> corrects) {
        Integer score = participant.getScore();
        ConcreteQuestion rootQuestion = question.getRoot();
        Integer correctValue = rootQuestion.getCorrectValue();
        Boolean allIsCorrect = !corrects.contains(false);
        
        if(allIsCorrect) {
            switch (participant.getCurrentDifficulty()) {
                case Difficulty.EASY:
                    score += correctValue * 100;
                    break;
                case Difficulty.NORMAL:
                    score += correctValue * 200;
                    break;
                case Difficulty.HARD:
                    score += correctValue * 300;
                    break;
                default:
                    break;
            };
        };

        return score;
    };
};