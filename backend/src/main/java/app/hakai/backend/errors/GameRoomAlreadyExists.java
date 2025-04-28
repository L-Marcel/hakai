package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class GameRoomAlreadyExists extends HttpError {
    public GameRoomAlreadyExists() {
        super(
            "Este jogo já possui uma sala!", 
            HttpStatus.CONFLICT
        );
    };
};
