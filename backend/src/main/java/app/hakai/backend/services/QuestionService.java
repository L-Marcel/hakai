package app.hakai.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.dtos.QuestionResponse;
import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.errors.QuestionNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.repositories.QuestionsRepository;

@Service
public class QuestionService {
    @Autowired
    private QuestionsRepository repository;

    public List<Question> getQuestionsByGame(Game game) throws GameNotFound {
        if(game == null) throw new GameNotFound();
        return repository.findAllByGame(game);
    };

    public Question getQuestionById(UUID uuid) throws QuestionNotFound {
        if(uuid == null) throw new QuestionNotFound();
        return repository.findByUuid(uuid).orElseThrow(() -> new QuestionNotFound());
    }
};
