package app.hakai.backend.dtos;

import java.util.List;

public record GameRequest( String title,
List<QuestionRequest> questions) {

}
