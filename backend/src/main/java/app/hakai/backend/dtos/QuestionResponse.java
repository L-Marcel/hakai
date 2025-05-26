package app.hakai.backend.dtos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.hakai.backend.models.Context;
import app.hakai.backend.models.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponse {
    private UUID uuid;
    private String question;
    private String answer;
    private List<String> context;

    public QuestionResponse(Question question) {
        this.uuid = question.getUuid();
        this.question = question.getQuestion();
        this.answer = question.getAnswer();
          this.context = question.getContexts().stream()
            .map(Context::getName)
            .collect(Collectors.toList());
    
    };
};
