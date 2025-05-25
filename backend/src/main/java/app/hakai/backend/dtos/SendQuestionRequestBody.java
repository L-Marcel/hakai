package app.hakai.backend.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendQuestionRequestBody {
    private String code;
    private UUID original; 
    private List<QuestionVariantResponse> variants;
}