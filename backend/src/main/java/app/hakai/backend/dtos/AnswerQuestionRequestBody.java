package app.hakai.backend.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerQuestionRequestBody {
    private UUID participant;
    private String answer;
};
