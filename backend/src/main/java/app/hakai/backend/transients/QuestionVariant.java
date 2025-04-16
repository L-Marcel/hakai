package app.hakai.backend.transients;

import java.util.List;

import app.hakai.backend.models.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionVariant {
    private int difficulty = 0;
    private List<String> options;
    private Question original;
};
