package app.hakai.backend.dtos.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGameRequestBody {
    private String title;
    private List<CreateQuestionRequestBody> questions;
};
