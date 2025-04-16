package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class GameNotFound extends HttpError {
    public GameNotFound() {
        super("Jogo não encontrado!", HttpStatus.NOT_FOUND);
    };
};
