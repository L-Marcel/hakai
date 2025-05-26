package app.hakai.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.dtos.GameRequest;
import app.hakai.backend.dtos.QuestionRequest;
import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Context;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.GameRepository;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ContextService contextService;
    
    public Game findGameById(UUID uuid) throws GameNotFound {
        return this.gameRepository.findById(uuid)
            .orElseThrow(GameNotFound::new);
    };

    public List<Game> findGamesByUser(User user) {
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    };

      public Game createGame(GameRequest request, User user) {
        Game game = new Game();
        game.setTitle(request.title());
        game.setOwner(user);

        List<Question> questions = new ArrayList<>();

        for (QuestionRequest qReq : request.questions()) {
            Question question = new Question();
            question.setGame(game);
            question.setQuestion(qReq.question());
            question.setAnswer(qReq.answer());

            if (qReq.context() != null && !qReq.context().isEmpty()) {
                List<Context> contexts = contextService.findByName(qReq.context());
                question.setContexts(contexts);
            }
            

            questions.add(question);
        }

        game.setQuestions(questions);

        return gameRepository.save(game);
    };

};
