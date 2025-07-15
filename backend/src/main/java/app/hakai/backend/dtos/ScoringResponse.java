package app.hakai.backend.dtos;

import java.util.UUID;

import org.kahai.framework.questions.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoringResponse {
    private UUID question;
    private int correctValue;
    private int wrongValue;

    public ScoringResponse(Question question) {
        this.question = question.getRoot().getUuid();
        this.correctValue = question.getRoot().getCorrectValue();
        this.wrongValue = question.getRoot().getWrongValue();
    }

}
