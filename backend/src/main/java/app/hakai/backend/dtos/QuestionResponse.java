package app.hakai.backend.dtos;

import java.util.UUID;

import app.hakai.backend.models.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponse {
    private UUID uuid;
    private String question;
    private String answer;

    public QuestionResponse(Question question) {
        this.uuid = question.getUuid();
        this.question = question.getQuestion();
        this.answer = question.getAnswer();
    };
};
