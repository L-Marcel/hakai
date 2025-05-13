package app.hakai.backend.errors;

import org.springframework.http.HttpStatus;

public class MissingFields extends HttpError {
    public MissingFields() {
        super("Todos os campos devem ser preenchidos.", HttpStatus.BAD_REQUEST);
    };
};
