package app.hakai.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.hakai.backend.models.User;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {
    public Optional<User> findByEmail(String email);
};
