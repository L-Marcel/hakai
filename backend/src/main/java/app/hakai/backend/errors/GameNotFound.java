package app.hakai.backend.errors;

public class GameNotFound extends RuntimeException {
    public GameNotFound(String gameId) {
        super("Game ID (" + gameId + ") not found.");
    }
}