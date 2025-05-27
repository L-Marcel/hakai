package app.hakai.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.annotations.RequireAuth;
import app.hakai.backend.dtos.request.LoginRequestBody;
import app.hakai.backend.dtos.request.RegisterRequestBody;
import app.hakai.backend.dtos.response.UserResponse;
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

        return ResponseEntity.ok(token);
    };

    @PostMapping
    public ResponseEntity<Void> createUser(
        @RequestBody RegisterRequestBody body
    ) {
        service.createUser(body);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    };

    @RequireAuth
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
        @AuthenticationPrincipal User user
    ) {
        UserResponse response = new UserResponse(user);
        return ResponseEntity.ok(response);
    };
};