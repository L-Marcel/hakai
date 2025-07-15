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
            System.out.println("Nenhuma variante disponível para seleção.");
            return new ArrayList<>();
        }

        System.out.println("Total de variantes recebidas: " + allAvailableVariants.size());

        Map<UUID, List<QuestionVariant>> variantsByQuestion = allAvailableVariants.stream()
                .collect(Collectors.groupingBy(variant -> variant.getRoot().getUuid()));

        System.out.println("Total de questões únicas: " + variantsByQuestion.size());

        int baseNumber = variantsByQuestion.size();
        int actualTotalToSend = baseNumber / 3;

        System.out.println("Total de questões a serem enviadas: " + actualTotalToSend);

        if (actualTotalToSend == 0) {
            return new ArrayList<>();
        }

        double easyPercentage = 0.3;
        double mediumPercentage = 0.4;

        int numHardToSelect = (int) Math.round(actualTotalToSend * (1.0 - easyPercentage - mediumPercentage));
        int numNormalToSelect = (int) Math.round(actualTotalToSend * mediumPercentage);
        int numEasyToSelect = actualTotalToSend - numHardToSelect - numNormalToSelect;

        System.out.println("Serão selecionadas:");
        System.out.println("Fáceis: " + numEasyToSelect);
        System.out.println("Médias: " + numNormalToSelect);
        System.out.println("Difíceis: " + numHardToSelect);

        List<QuestionVariant> finalSelectedVariants = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>(variantsByQuestion.keySet());

        for (UUID question : uuidList) {
            List<QuestionVariant> variantsForThisQuestion = variantsByQuestion.get(question);

            if (numHardToSelect > 0) {
                for (QuestionVariant variant : variantsForThisQuestion) {
                    if (variant.getRoot().getDifficulty() == Difficulty.HARD) {
                        finalSelectedVariants.add(variant);
                        numHardToSelect--;
                        System.out.println(
                                "Selecionada HARD da questão " + question);
                        break;
                    }
                }

            } else if (numNormalToSelect > 0) {
                for (QuestionVariant variant : variantsForThisQuestion) {
                    if (variant.getRoot().getDifficulty() == Difficulty.NORMAL) {
                        finalSelectedVariants.add(variant);
                        numNormalToSelect--;
                        System.out.println(
                                "Selecionada NORMAL da questão " + question);
                        break;
                    }
                }

            } else if (numEasyToSelect > 0) {
                for (QuestionVariant variant : variantsForThisQuestion) {
                    if (variant.getRoot().getDifficulty() == Difficulty.EASY) {
                        finalSelectedVariants.add(variant);
                        numEasyToSelect--;
                        System.out.println(
                                "Selecionada EASY da questão " + question);
                        break;
                    }
                }
            }
        }

        Collections.shuffle(finalSelectedVariants);
        System.out.println("Final total selecionado: " + finalSelectedVariants.size());
        return finalSelectedVariants;
    }
}