package app.hakai.backend.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGameRequest {

    private String title;
    private List<CreateQuestionRequest> questions;

}
