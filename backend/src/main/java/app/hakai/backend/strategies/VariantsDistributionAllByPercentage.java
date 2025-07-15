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

public class VariantsDistributionAllByPercentage implements VariantsDistributionStrategy {
    private static final double EASY_PERCENTAGE = 0.3;
    private static final double MEDIUM_PERCENTAGE = 0.4;
    private final Random random = new Random();

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
                .collect(Collectors.groupingBy(variant -> variant.getRoot().getUuid()));

        List<List<QuestionVariant>> easyQuestionPool = new ArrayList<>();
        List<List<QuestionVariant>> mediumQuestionPool = new ArrayList<>();
        List<List<QuestionVariant>> hardQuestionPool = new ArrayList<>();
        variantsByQuestion.values().forEach(variantList -> {
            Difficulty difficulty = variantList.get(0).getRoot().getDifficulty();
            switch (difficulty) {
                case EASY:
                    easyQuestionPool.add(variantList);
                    break;
                case NORMAL:
                    mediumQuestionPool.add(variantList);
                    break;
                case HARD:
                    hardQuestionPool.add(variantList);
                    break;
            }
        });

        int baseNumber = variantsByQuestion.size();
        int actualTotalToSend = baseNumber / 3;

        if (actualTotalToSend == 0) {
            return new ArrayList<>();
        }

        int numEasyToSelect = (int) Math.round(actualTotalToSend * EASY_PERCENTAGE);
        int numMediumToSelect = (int) Math.round(actualTotalToSend * MEDIUM_PERCENTAGE);
        int numHardToSelect = actualTotalToSend - numEasyToSelect - numMediumToSelect;

        List<QuestionVariant> finalSelectedVariants = new ArrayList<>();

        addRandomVariants(hardQuestionPool, numHardToSelect, finalSelectedVariants);
        addRandomVariants(mediumQuestionPool, numMediumToSelect, finalSelectedVariants);
        addRandomVariants(easyQuestionPool, numEasyToSelect, finalSelectedVariants);

        Collections.shuffle(finalSelectedVariants);
        return finalSelectedVariants;
    }

    private void addRandomVariants(List<List<QuestionVariant>> questionPool, int countToSelect,
            List<QuestionVariant> finalSelectionList) {

        Collections.shuffle(questionPool);

        int limit = Math.min(countToSelect, questionPool.size());

        for (int i = 0; i < limit; i++) {
            List<QuestionVariant> variantsForOneQuestion = questionPool.get(i);
            QuestionVariant randomVariant = variantsForOneQuestion.get(random.nextInt(variantsForOneQuestion.size()));
            finalSelectionList.add(randomVariant);
        }
    }
}