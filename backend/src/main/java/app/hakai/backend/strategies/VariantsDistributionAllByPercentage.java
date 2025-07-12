package app.hakai.backend.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;

public class VariantsDistributionAllByPercentage implements VariantsDistributionStrategy {

    @Override
    public Optional<QuestionVariant> selectVariant(Participant participant, List<QuestionVariant> variants) {
        Collections.shuffle(variants);
        return variants.size() > 0? Optional.of(variants.getFirst()) : Optional.empty();
    };

    @Override
    public List<QuestionVariant> selectVariants(Participant participant, List<QuestionVariant> variants) {
        if (variants == null || variants.isEmpty()) {
            return Collections.emptyList();
        };

        List<QuestionVariant> hardVariants = new ArrayList<>();
        List<QuestionVariant> mediumVariants = new ArrayList<>();
        List<QuestionVariant> easyVariants = new ArrayList<>();

        for (QuestionVariant variant : variants) {
            switch (variant.getRoot().getDifficulty()) {
                case HARD:
                    hardVariants.add(variant);
                    break;
                case NORMAL:
                    mediumVariants.add(variant);
                    break;
                case EASY:
                    easyVariants.add(variant);
                    break;
            };
        };

        int totalSize = variants.size();
        int hardCount = (int) Math.round(totalSize * 0.30);
        int mediumCount = (int) Math.round(totalSize * 0.40);
        int easyCount = totalSize - hardCount - mediumCount;

        List<QuestionVariant> selectedVariants = new ArrayList<>();
        Collections.shuffle(hardVariants);
        Collections.shuffle(mediumVariants);
        Collections.shuffle(easyVariants);

        selectedVariants.addAll(hardVariants.subList(0, Math.min(hardCount, hardVariants.size())));
        selectedVariants.addAll(mediumVariants.subList(0, Math.min(mediumCount, mediumVariants.size())));
        selectedVariants.addAll(easyVariants.subList(0, Math.min(easyCount, easyVariants.size())));

        Collections.shuffle(selectedVariants);
        return selectedVariants;
    };
};