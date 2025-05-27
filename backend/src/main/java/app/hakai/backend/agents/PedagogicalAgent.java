package app.hakai.backend.agents;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.hakai.backend.events.RoomEventPublisher;
import app.hakai.backend.models.Question;
import app.hakai.backend.transients.QuestionVariant;
import app.hakai.backend.transients.Room;

@Component
public class PedagogicalAgent {
    @Autowired
    private Chatbot chatbot;

    @Autowired
    private RoomEventPublisher publisher;

    @Autowired
    private ObjectMapper objectMapper;

    private final String SYSTEM_PROMPT = """
        Você é um agente inteligente responsável por criar de variações de questões enviadas por um professor que desejam avaliar seus alunos.

        Ele enviará uma questão, algumas palavras chaves para dar contexto a ela e a resposta certa. Com esses dados você deve criar variações desta questão nos seguintes níveis de dificuldade:

        • Fácil (EASY): pergunta mais simples com 3 ou 4 opções fáceis (incluindo a certa)
        • Médio (NORMAL): pergunta um pouco mais desafiadora com 4 ou 5 opções medianas para acertar (incluindo a certa)
        • Difícil (HARD): pergunta com formulação mais elaborada ou técnica com 6 opções difíceis de acertar (incluindo a certa)

        Identifique as variações em dificuldade com os valores EASY, NORMAL e HARD definidos acima.

        Respeite todas as regras abaixo:

        REGRA 1: Como resposta, apenas gere um json no seguinte formato (e não coloque dentro de bloco de código, retorne apenas com texto):
        [
            {
                "difficulty": "EASY",
                "question": "...",
                "options": [
                "...",
                "...",
                "..."
                ]
            },
            {
                "difficulty": "NORMAL",
                "question": "...",
                "options": [
                "...",
                "...",
                "...",
                "..."
                ]
            },
            {
                "difficulty": "HARD",
                "question": "...",
                "options": [
                "...",
                "...",
                "...",
                "...",
                "..."
                ]
            }
        ]

        REGRA 2: A resposta certa informada pelo professor SEMPRE deve aparecer EXATAMENTE igual nas opções de resposta de cada uma das variações geradas.

        REGRA 3: Não coloque prefixos de listagem nas respostas, ex: A. B. C.; I. II. III. ou 1. 2. 3.

        REGRA 4: Se atenta as palavras chaves passadas pelo professor para dar contexto e procure não fugir deste contexto ao gerar as variações.
            
        REGRA 5: Não coloque perguntas como opções de resposta, exceto que a resposta correta também seja uma pergunta.
    """;

    private String buildPrompt(Question question) {
        return String.format(
            """
            Questão: %s
            Palavras-chaves de contexto: %s
            Resposta certa: %s
            """, 
            question.getQuestion(),
            question.getContexts(), 
            question.getAnswer()
        );
    };

    public void generateRoomQuestionsVariants(
        Question question,   
        Room room,
        PedagogicalAgentCallback callback
    ) {
        String prompt = buildPrompt(question);

        publisher.emitGenerationStatus(
            room, 
            "Gerando variações"
        );

        chatbot.request(
            this.SYSTEM_PROMPT, 
            prompt, 
            (response) -> {
                publisher.emitGenerationStatus(
                    room, 
                    "Recebendo resposta da IA"
                );

                response.ifPresentOrElse((output) -> {
                    publisher.emitGenerationStatus(
                        room, 
                        "Formatando respota da IA"
                    );

                    String json = output
                        .replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1")
                        .trim();
                    
                    try {
                        List<QuestionVariant> variants = objectMapper.readValue(
                            json,
                            new TypeReference<List<QuestionVariant>>() {}
                        );

                        variants.forEach(variant -> variant.setOriginal(question));

                        publisher.emitGenerationStatus(
                            room, 
                            "Variações geradas com sucesso"
                        );

                        callback.accept(variants);
                    } catch (IOException e) {
                        publisher.emitGenerationStatus(
                            room, 
                            "Formato invalido recebido pela IA"
                        );

                        e.printStackTrace();
                    }
                }, () -> {
                    publisher.emitGenerationStatus(
                        room, 
                        "Falha ao gerar variações"
                    );
                });
            }
        );
    };
};