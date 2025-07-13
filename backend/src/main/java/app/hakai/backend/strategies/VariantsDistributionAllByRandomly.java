package app.hakai.backend.strategies;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;

public class VariantsDistributionAllByRandomly implements VariantsDistributionStrategy {

    private Difficulty radomDifficulty() {
        Random random = new Random();

        Difficulty[] difficulties = Difficulty.values();

        int randomIndex = random.nextInt(difficulties.length);

        return difficulties[randomIndex];
    }

    @Override
    public Optional<QuestionVariant> selectVariant(
        Participant participant, 
        List<QuestionVariant> variants
    ) {
        return variants.stream()
            .filter(
                (variant) -> variant.getRoot().getDifficulty() == radomDifficulty()
            ).findAny();
    };
    
    @Override
    public List<QuestionVariant> selectVariants(
        Participant participant, 
        List<QuestionVariant> variants
    ) {
        return variants.stream()
            .filter(
                (variant) -> variant.getRoot().getDifficulty() == radomDifficulty()
            ).collect(Collectors.toList());
    };
};
