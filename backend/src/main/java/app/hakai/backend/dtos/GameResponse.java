package app.hakai.backend.dtos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.hakai.backend.models.Game;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResponse {
    private UUID uuid;
    private UUID owner;
    private String title;
    private List<QuestionResponse> questions;

    public GameResponse(Game game) {
        this.uuid = game.getUuid();
        this.owner = game.getOwner().getUuid();
        this.title = game.getTitle();
        this.questions = game.getQuestions()
                .stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    };
};
