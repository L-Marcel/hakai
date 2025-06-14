package app.hakai.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.hakai.backend.auth.JwtUtil;
import app.hakai.backend.dtos.request.RegisterRequestBody;
import app.hakai.backend.errors.EmailAlreadyInUse;
import app.hakai.backend.errors.InvalidCredentials;
import app.hakai.backend.errors.InvalidEmail;
import app.hakai.backend.errors.MissingFields;
import app.hakai.backend.errors.WeakPassword;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User createUser(
        RegisterRequestBody body
    ) throws MissingFields, InvalidEmail, WeakPassword, EmailAlreadyInUse {
        String email = body.getEmail();
        String password = body.getPassword();
        String name = body.getName();
        
        if(
            email == null || email.isBlank() ||
            password == null || password.isBlank() ||
            name == null || name.isBlank()
        ) throw new MissingFields();

        if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new InvalidEmail();

        if(password.length() < 6)
            throw new WeakPassword();

        if(repository.findByEmail(email).isPresent())
            throw new EmailAlreadyInUse();

        User user = new User(email, encoder.encode(password), name);
        repository.save(user);

        return user;
    };

    public String login(
        String email, 
        String password
    ) throws InvalidCredentials {
        User user = repository.findByEmail(email)
            .orElseThrow(InvalidCredentials::new);

        if(!encoder.matches(password, user.getPassword()))
            throw new InvalidCredentials();

        return jwtUtil.generateToken(user.getUuid());
    };
};
