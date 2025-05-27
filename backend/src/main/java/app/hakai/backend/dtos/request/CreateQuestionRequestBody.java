package app.hakai.backend.dtos.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionRequestBody {
    private String question;
    private String answer;
    private List<String> context;
};
