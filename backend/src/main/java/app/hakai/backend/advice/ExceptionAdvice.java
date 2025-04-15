package app.hakai.backend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import app.hakai.backend.errors.HttpError;


@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(HttpError.class)
    public ResponseEntity<Object> handleGameNotFound (HttpError error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getError());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNotFound() {
        HttpError error = new HttpError("Route not found", HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getError());
    }

}
