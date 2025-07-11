package app.hakai.backend.question.dtos;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.questions.response.QuestionResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWithFeedbackResponse implements QuestionResponse{
    private UUID uuid;
    private String question;
    private List<String> contexts;
    private List<String> answers;
    private String feedback;
}
