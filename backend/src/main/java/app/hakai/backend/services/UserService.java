package app.hakai.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.hakai.backend.auth.JwtUtil;
import app.hakai.backend.errors.EmailAlreadyInUse;
import app.hakai.backend.errors.UserNotFound;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.UsersRepository;

@Service
public class UserService {
    @Autowired
    private UsersRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void register(String email, String password, String name) {
        if(repository.findByEmail(email).isPresent()) 
            throw new EmailAlreadyInUse();
        User user = new User(email, encoder.encode(password), name);
        repository.save(user);
    };

    public String login(String email, String password) throws UserNotFound {
        User user = repository.findByEmail(email).orElseThrow(
            () -> new UserNotFound()
        );

        if(!encoder.matches(password, user.getPassword()))
            throw new UserNotFound();
        
        return jwtUtil.generateToken(user.getUuid());
    };
};
