package app.hakai.backend.dtos;

import java.util.List;

public record QuestionRequest( 
     String question,
    String answer,
     List<String> context) {

}
