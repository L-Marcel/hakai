package app.hakai.backend.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionRequest {

     private String question;
     private String answer;
     private List<String> context;
}
