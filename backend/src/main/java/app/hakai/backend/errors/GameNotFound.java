package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GameNotFound extends RuntimeException {
    public GameNotFound(String gameId) {
        super("Game ID (" + gameId + ") not found.");
    }
}