package app.hakai.backend.transients;

import java.util.List;

import app.hakai.backend.models.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionVariant {
    private int difficulty = 0;
    private List<String> options;
    private Question original;
}
