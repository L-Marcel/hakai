package app.hakai.backend.strategies;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.QuestionVariant;

public class VariantsDistributionByDifficulty implements VariantsDistributionStrategy {
    @Override
    public Optional<QuestionVariant> selectVariant(
        Participant participant, 
        List<QuestionVariant> variants
    ) {
        return variants.stream()
            .filter(
                (variant) -> variant.getDifficulty() == participant.getCurrentDifficulty()
            ).findAny();
    };
    
    @Override
    public List<QuestionVariant> selectVariants(
        Participant participant, 
        List<QuestionVariant> variants
    ) {
        return variants.stream()
            .filter(
                (variant) -> variant.getDifficulty() == participant.getCurrentDifficulty()
            ).collect(Collectors.toList());
    };
};
