package app.hakai.backend.errors;

public class GameNotFound extends RuntimeException {
    public GameNotFound() {
        super("Game not found.");
    }
}
