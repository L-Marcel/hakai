package app.hakai.backend.agents;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.QuestionVariant;

public class PedagogicalAgent {
    @Autowired
    private Chatbot chatbot;

    //private String prompt;
    private RoomService roomService;
    private GameService gameService;

    public PedagogicalAgent(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }    

    private String buildPrompt(Question question) {
        return String.format("""
            Você é uma inteligência artificial que irá ajudar um usuário na criação de variações de questões.
            O usuário enviará a questão com a resposta certa.
            Você deve criar variações nos níveis fácil, médio e difícil dessa questão.
            • Fácil (F): 3 opções (incluindo a certa)
            • Médio (M): 4 opções (incluindo a certa)
            • Difícil (D): 5 opções (incluindo a certa)
            Identifique as variações com prefixo "F-", "M-" ou "D-".
            Para cada opção de resposta, comece a linha com "#".
            
            Questão: %s
            Resposta certa: %s
            """, question.getQuestion(), question.getAnswer());
    }

    private Question[] convertOutputTQuestionVariants(String output) {
        return null;
    }

    public void generateRoomQuestionsVariants(UUID code) {
        Game game = gameService.getGame(code);
        
        if (game == null) {
            throw new GameNotFound();
        }

        for (Question q : game.getQuestions()) {
            String prompt = buildPrompt(q);

            chatbot.request(prompt, responseOpt -> {
                responseOpt.ifPresent(output -> {
                    //generative
                });
            });
        }
    }

}
