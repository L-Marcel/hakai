package app.hakai.backend.errors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class HttpError extends RuntimeException{
    private HttpStatus status;

    public HttpError (String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public Map<String, Object> getError() {
        Map<String, Object> error = new LinkedHashMap<>();
        
        error.put("status", status.value());
        error.put("message", this.getMessage());

        return error;
    }

    @Override
    public String toString(){
        return "Status: "+status+"\nMessage: "+this.getMessage();
    }
}
