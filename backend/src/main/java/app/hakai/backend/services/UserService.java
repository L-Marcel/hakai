package app.hakai.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.User;
import app.hakai.backend.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    /*
     * private User getUser(String email) {
     * userRepository.findById(email);
     * }
     */

    private void autenticate(String email, String senha) {

    }

    public void registerUser(User usuarioDTO) {
        userRepository.save(usuarioDTO);
    }

    public User login(String email, String senha) throws Exception {
        Optional<User> optionalUser = userRepository.findById(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getSenha().equals(senha)) {
                return user; // Login bem-sucedido
            } else {
                throw new Exception("Senha incorreta");
            }
        } else {
            throw new Exception("Usuário não encontrado");
        }
    }
}
