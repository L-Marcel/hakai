package app.hakai.backend.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;

public class VariantsDistributionAllByRandomly implements VariantsDistributionStrategy {

    private Difficulty randomDifficulty() {
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
        // única variante não é aplicável.
        return Optional.empty();
    };
    
    @Override
    public List<QuestionVariant> selectVariants(Participant participant, List<QuestionVariant> allAvailableVariants) {
        if (allAvailableVariants == null || allAvailableVariants.isEmpty()) {
            return new ArrayList<>();
        }

        Map<UUID, List<QuestionVariant>> variantsByQuestion = allAvailableVariants.stream()
                .collect(Collectors.groupingBy(variant -> variant.getRoot().getOriginal().getRoot().getUuid()));

        List<QuestionVariant> selectedVariants = new ArrayList<>();

        for (Map.Entry<UUID, List<QuestionVariant>> entry : variantsByQuestion.entrySet()) {
            List<QuestionVariant> variants = entry.getValue();
            Difficulty chosenDifficulty = randomDifficulty();

            QuestionVariant selected = null;

            for (QuestionVariant variant : variants) {
                if (variant.getRoot().getDifficulty() == chosenDifficulty) {
                    selected = variant;
                    break;
                }
            }

            if (selected == null && !variants.isEmpty()) {
                selected = variants.get(0);
            }

            if (selected != null) {
                selectedVariants.add(selected);
            }
        }

        Collections.shuffle(selectedVariants);
        return selectedVariants;
    };
};