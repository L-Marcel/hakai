package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class Unauthorized extends HttpError {
    public Unauthorized() {
        super("Não autorizado!", HttpStatus.UNAUTHORIZED);
    };
};
