package app.hakai.backend.question.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendAllQuestionsRequest {
    private String code;
    private List<QuestionPayload> questions;
}