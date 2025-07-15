package app.hakai.backend.dtos;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.kahai.framework.questions.variants.QuestionVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionPayload {
    private UUID original;
    private List<QuestionVariant> variants;
}