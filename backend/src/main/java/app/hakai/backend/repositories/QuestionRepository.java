package app.hakai.backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    public List<Question> findAllByGame(Game game);
    public Optional<Question> findByUuid(UUID uuid);
};
