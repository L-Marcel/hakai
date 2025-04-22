package app.hakai.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.dtos.LoginRequestBody;
import app.hakai.backend.models.User;
import app.hakai.backend.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @RequestBody LoginRequestBody body
    ) {
        String token = service.login(
            body.getEmail(), 
            body.getPassword()
        );

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(token);
    };

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        service.register(user);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Usuário registrado com sucesso!");
    };
};
