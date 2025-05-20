package app.hakai.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.agents.PedagogicalAgent;
import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.errors.QuestionNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.repositories.QuestionsRepository;
import app.hakai.backend.transients.Room;

@Service
public class QuestionService {
    @Autowired
    private QuestionsRepository repository;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PedagogicalAgent pedagogicalAgent;

    public List<Question> getQuestionsByGame(Game game) throws GameNotFound {
        if(game == null) throw new GameNotFound();
        return repository.findAllByGame(game);
    };

    public Question getQuestionById(UUID uuid) throws QuestionNotFound {
        if(uuid == null) throw new QuestionNotFound();
        return repository.findByUuid(uuid).orElseThrow(() -> new QuestionNotFound());
    };

    public void generateQuestionVariants(Question question, Room room) {
        pedagogicalAgent.generateRoomQuestionsVariants(question, variants -> {
            messagingService.sendVariantsToOwner(room, variants);
        });
    };
};
