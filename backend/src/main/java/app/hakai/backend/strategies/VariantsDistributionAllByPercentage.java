package app.hakai.backend.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;

public class VariantsDistributionAllByPercentage implements VariantsDistributionStrategy {

    @Override
    public Optional<QuestionVariant> selectVariant(Participant participant, List<QuestionVariant> variants) {
        // única variante não é aplicável.
        return Optional.empty();
    }

    @Override
    public List<QuestionVariant> selectVariants(Participant participant, List<QuestionVariant> allAvailableVariants) {
        if (allAvailableVariants == null || allAvailableVariants.isEmpty()) {
            return new ArrayList<>();
        }

        Map<UUID, List<QuestionVariant>> variantsByQuestion = allAvailableVariants.stream()
                .collect(Collectors.groupingBy(variant -> variant.getRoot().getOriginal().getRoot().getUuid()));
        int baseNumber = variantsByQuestion.size();

        double easyPercentage = 0.3;
        double mediumPercentage = 0.4;

        int numHardToSelect = (int) Math.round(baseNumber * (1.0 - easyPercentage - mediumPercentage));
        int numNormalToSelect = (int) Math.round(baseNumber * mediumPercentage);
        int numEasyToSelect = baseNumber - numHardToSelect - numNormalToSelect;

        List<QuestionVariant> finalSelectedVariants = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>(variantsByQuestion.keySet());

        for (UUID question : uuidList) {
            List<QuestionVariant> variantsForThisQuestion = variantsByQuestion.get(question);

            if (numHardToSelect > 0) {
                for (QuestionVariant variant : variantsForThisQuestion) {
                    if (variant.getRoot().getDifficulty() == Difficulty.HARD) {
                        finalSelectedVariants.add(variant);
                        numHardToSelect--;

                        break;
                    }
                }

            } else if (numNormalToSelect > 0) {
                for (QuestionVariant variant : variantsForThisQuestion) {
                    if (variant.getRoot().getDifficulty() == Difficulty.NORMAL) {
                        finalSelectedVariants.add(variant);
                        numNormalToSelect--;
                        break;
                    }
                }

            } else if (numEasyToSelect > 0) {
                for (QuestionVariant variant : variantsForThisQuestion) {
                    if (variant.getRoot().getDifficulty() == Difficulty.EASY) {
                        finalSelectedVariants.add(variant);
                        numEasyToSelect--;

                        break;
                    }
                }
            }
        }

        Collections.shuffle(finalSelectedVariants);
        return finalSelectedVariants;
    }
}