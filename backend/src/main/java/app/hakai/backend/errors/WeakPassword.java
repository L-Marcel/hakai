package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class WeakPassword extends HttpError {
    public WeakPassword() {
        super("Senha deve ter no mínimo 6 caracteres.", HttpStatus.BAD_REQUEST);
    }
}
