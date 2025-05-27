package app.hakai.backend.dtos.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerQuestionRequestBody {
    private UUID question;
    private UUID participant;
    private String answer;
};
