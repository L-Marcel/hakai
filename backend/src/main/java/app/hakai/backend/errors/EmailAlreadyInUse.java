package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUse extends HttpError {
    public EmailAlreadyInUse() {
        super("Email já está em uso!", HttpStatus.CONFLICT);
    };
};