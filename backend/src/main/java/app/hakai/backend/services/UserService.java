package app.hakai.backend.services;

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

    public void registrarUsuario(User usuarioDTO) {
        userRepository.save(usuarioDTO);
    }
}
