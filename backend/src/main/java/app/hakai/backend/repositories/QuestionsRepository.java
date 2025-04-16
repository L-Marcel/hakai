package app.hakai.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;

@Repository
public interface QuestionsRepository extends JpaRepository<Question, Game> {
    public List<Question> findAllByGame(Game game);
};
