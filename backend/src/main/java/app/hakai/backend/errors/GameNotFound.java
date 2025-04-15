package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class GameNotFound extends HttpError {
    public GameNotFound() {
        super("Game not found", HttpStatus.NOT_FOUND);
    }
}
