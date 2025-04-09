package app.hakai.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.Question;
import app.hakai.backend.repositories.QuestionRepository;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository repository;

    public List<Question> getQuestionsByGameUUID(UUID uuid) {
        return repository.findAllByGame(uuid);
    }
}
