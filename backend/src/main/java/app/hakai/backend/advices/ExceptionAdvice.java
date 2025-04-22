package app.hakai.backend.advices;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import app.hakai.backend.errors.HttpError;
import app.hakai.backend.errors.MethodNotAllowed;
import app.hakai.backend.errors.RouteNotFound;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(HttpError.class)
    public ResponseEntity<Map<String, Object>> handleHttpErrors(HttpError error) {
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerNotFound() {
        RouteNotFound error = new RouteNotFound();
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerMethodNotAllowed() {
        MethodNotAllowed error = new MethodNotAllowed();
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };
};
