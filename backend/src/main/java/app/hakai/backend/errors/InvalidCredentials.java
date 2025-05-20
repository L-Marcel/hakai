package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class InvalidCredentials extends HttpError {
    public InvalidCredentials() {
        super("Email ou senha inválidos.", HttpStatus.UNAUTHORIZED);
    };
};
