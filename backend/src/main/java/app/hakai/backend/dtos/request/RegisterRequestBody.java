package app.hakai.backend.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestBody {
    private String email;
    private String password;
    private String name;
};