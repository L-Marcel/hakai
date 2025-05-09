package app.hakai.backend.agents;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.hakai.backend.errors.QuestionNotFound;
import app.hakai.backend.models.Question;
import app.hakai.backend.services.MessagingService;
import app.hakai.backend.services.QuestionService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.QuestionVariant;
import app.hakai.backend.transients.Room;


@Component
public class PedagogicalAgent {
    @Autowired
    private Chatbot chatbot;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private MessagingService messagingService;


    private String buildPrompt(Question question) {
        return String.format("""
            Você irá ajudar na criação de variações de questões.
            Será enviado uma questão com a resposta certa e você deve criar variações nos níveis fácil, médio e difícil dessa questão.
            • Fácil (1): pergunta mais simples com 3-4 opções fáceis (incluindo a certa)
            • Médio (2): pergunta um pouco mais desafiadora com 4-5 opções medianas para acertar (incluindo a certa)
            • Difícil (3): pergunta com formulação mais elaborada ou técnica com 6 opções difíceis de acertar (incluindo a certa)
            Identifique as variações em dificuldade com os valores numérico acima definidos.
            Como resposta, apenas gere um json no seguinte formato:
            [
                {
                    "difficulty": 1,
                    "questionVariant": "...",
                    "options": [
                    "...",
                    "...",
                    "..."
                    ]
                },
                {
                    "difficulty": 2,
                    "questionVariant": "...",
                    "options": [
                    "...",
                    "...",
                    "...",
                    "..."
                    ]
                },
                {
                    "difficulty": 3,
                    "questionVariant": "...",
                    "options": [
                    "...",
                    "...",
                    "...",
                    "...",
                    "..."
                    ]
                }
            ]
            
            Questão: %s
            Resposta certa: %s
            """, question.getQuestion(), question.getAnswer());
    }

    public void generateRoomQuestionsVariants(UUID code) {
        Question question = questionService.getQuestionById(code);
        
        if (question == null) {
            throw new QuestionNotFound();
        }
        
        String prompt = buildPrompt(question);

        String stringUuidGame = questionService.getQuestionById(code).getGame().getUuid().toString();

        Room room = roomService.getRoom(stringUuidGame);

        chatbot.request(prompt, responseOpt -> {
            responseOpt.ifPresent(output -> {
                try {
                    List<QuestionVariant> list = objectMapper.readValue(output, new TypeReference<List<QuestionVariant>>() {});

                    for(QuestionVariant variant : list) {
                        variant.setOriginal(question);
                    };

                    messagingService.sendVariantsToOwner(room, list);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            

            });
        });

    }

}