package app.hakai.backend.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionVariantRequestBody {
    private UUID uuid;
    private String question;
    private int difficulty;
    private List<String> options;
    private List<String> context;
    private UUID original; 
}