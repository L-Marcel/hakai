package app.hakai.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;

import app.hakai.backend.models.User;
import app.hakai.backend.services.UserService;

public class UserController {
    @Autowired
    private UserService userService;

    private ResponseEntity<User> login(String email, String senha) {
        return null;
    }
}
