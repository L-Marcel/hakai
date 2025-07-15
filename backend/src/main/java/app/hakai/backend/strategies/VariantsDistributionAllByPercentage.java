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

        int baseNumber = variantsByQuestion.size();
        int actualTotalToSend = baseNumber / 3;

        if (actualTotalToSend == 0) {
            return new ArrayList<>();
        }
        double easyPercentage = 0.3;
        double mediumPercentage = 0.4;

        int numHardToSelect = (int) Math.round(actualTotalToSend * (1.0 - easyPercentage - mediumPercentage));
        int numMediumToSelect = (int) Math.round(actualTotalToSend * mediumPercentage);
        int numEasyToSelect = actualTotalToSend - numHardToSelect - numMediumToSelect;

        List<QuestionVariant> finalSelectedVariants = new ArrayList<>();
        Set<UUID> usedQuestionUuids = new HashSet();

        List<List<QuestionVariant>> shuffledQuestions = new ArrayList<>(variantsByQuestion.values());
        Collections.shuffle(shuffledQuestions);
        findAndAddQuestionsByDifficulty(shuffledQuestions, Difficulty.HARD, numHardToSelect, finalSelectedVariants,
                usedQuestionUuids);
        findAndAddQuestionsByDifficulty(shuffledQuestions, Difficulty.NORMAL, numMediumToSelect, finalSelectedVariants,
                usedQuestionUuids);
        findAndAddQuestionsByDifficulty(shuffledQuestions, Difficulty.EASY, numEasyToSelect, finalSelectedVariants,
                usedQuestionUuids);

        Collections.shuffle(finalSelectedVariants);
        return finalSelectedVariants;
    }

    private void findAndAddQuestionsByDifficulty(
            List<List<QuestionVariant>> allQuestions,
            Difficulty targetDifficulty,
            int countToSelect,
            List<QuestionVariant> finalSelectionList,
            Set<UUID> usedQuestionUuids) {

        int foundCount = 0;
        for (List<QuestionVariant> variantsForOneQuestion : allQuestions) {
            if (foundCount >= countToSelect) {
                break;
            }

            QuestionVariant representativeVariant = variantsForOneQuestion.get(0);
            UUID questionUuid = representativeVariant.getRoot().getUuid();
            if (representativeVariant.getRoot().getDifficulty() == targetDifficulty
                    && !usedQuestionUuids.contains(questionUuid)) {
                QuestionVariant randomVariant = variantsForOneQuestion
                        .get(random.nextInt(variantsForOneQuestion.size()));

                finalSelectionList.add(randomVariant);
                usedQuestionUuids.add(questionUuid);

                foundCount++;
            }
        }
    }
}