package app.hakai.backend.dtos;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import app.hakai.backend.transients.QuestionVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionVariantResponse {
    private UUID uuid;
    private String question;
    private int difficulty = 0;
    private List<String> options;
    private List<String> context;
    private UUID original;
    private String answer;

    public QuestionVariantResponse(QuestionVariant question, boolean hasAnswer){
        this.uuid = question.getUuid();
        this.question = question.getQuestion();
        this.difficulty = question.getDifficulty();
        this.options = question.getOptions();
        this.original = question.getOriginal().getUuid();
        if (hasAnswer) {
            this.answer = question.getOriginal().getAnswer();
            this.context = new LinkedList<>();
        };
    };

    public QuestionVariantResponse() {};
};
