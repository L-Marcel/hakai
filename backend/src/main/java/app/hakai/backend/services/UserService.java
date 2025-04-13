package app.hakai.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.User;
import app.hakai.backend.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void

            autenticate(String email, String senha) {

    }

    public void register(User user) {
        if (userRepository.findById(user.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }
        user.setSenha(encoder.encode(user.getSenha()));
        userRepository.save(user);
    }

    public User login(String email, String senha) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!encoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
        return user;

    }

}
