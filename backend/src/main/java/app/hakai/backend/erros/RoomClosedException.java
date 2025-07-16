package app.hakai.backend.erros;

import org.kahai.framework.errors.HttpError;
import org.springframework.http.HttpStatus;

public class RoomClosedException extends HttpError {
    public RoomClosedException(String message) {
        super(message, HttpStatus.CONFLICT);
    };
};
