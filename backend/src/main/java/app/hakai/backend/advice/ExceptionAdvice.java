package app.hakai.backend.advice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import app.hakai.backend.errors.GameNotFound;
import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(GameNotFound.class)
    public ResponseEntity<Object> handleGameNotFound (GameNotFound ex, HttpServletRequest request) {
        Map<String, Object> error = new LinkedHashMap<>();
        
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        error.put("message", ex.getMessage());
        error.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
