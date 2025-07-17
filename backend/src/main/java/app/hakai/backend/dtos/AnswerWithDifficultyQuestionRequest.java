package app.hakai.backend.dtos;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Difficulty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerWithDifficultyQuestionRequest {
    private UUID question;
    private Difficulty difficulty;
    private UUID participant;
    private List<String> answers;
};
