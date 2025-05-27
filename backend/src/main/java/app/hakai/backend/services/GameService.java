package app.hakai.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.dtos.request.CreateGameRequestBody;
import app.hakai.backend.dtos.request.CreateQuestionRequestBody;
import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Context;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.ContextRepository;
import app.hakai.backend.repositories.GameRepository;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ContextRepository contextRepository;

    public Game findGameById(UUID uuid) throws GameNotFound {
        return this.gameRepository.findById(uuid)
            .orElseThrow(GameNotFound::new);
    };

    public List<Game> findGamesByUser(User user) {
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    };

    public Game createGame(
        CreateGameRequestBody body,
        User user
    ) {
        String title = body.getTitle();
        
        Game game = new Game();
        game.setTitle(title);
        game.setOwner(user);

        List<Question> questions = new ArrayList<>();
        List<CreateQuestionRequestBody> questionBodies = body.getQuestions();
        for (CreateQuestionRequestBody questionBody : questionBodies) {
            Question question = new Question();
            question.setGame(game);
            question.setQuestion(questionBody.getQuestion());
            question.setAnswer(questionBody.getAnswer());

            List<String> contextNames = questionBody.getContext();
            if (contextNames != null && !contextNames.isEmpty()) {
                List<Context> contexts = contextNames
                    .stream()
                    .map(Context::new)
                    .map(
                        (context) -> contextRepository
                            .findByName(context.getName())
                            .orElse(contextRepository.save(context))
                    ).collect(Collectors.toList());

                question.setContexts(contexts);
            };

            questions.add(question);
        };

        game.setQuestions(questions);
        gameRepository.save(game);

        return game;
    };
};
