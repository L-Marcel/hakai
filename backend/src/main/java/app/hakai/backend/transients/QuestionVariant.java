package app.hakai.backend.transients;

import java.util.List;
import java.util.UUID;

import app.hakai.backend.models.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionVariant {
    private UUID uuid = UUID.randomUUID();
    private String question;
    private int difficulty = 0;
    private List<String> options;
    private Question original;

    public QuestionVariant() {
        this.uuid = UUID.randomUUID();
    };
};
