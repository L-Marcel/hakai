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


@Component
public class PedagogicalAgent {
    @Autowired
    private Chatbot chatbot;
 @Autowired
    private RoomEventPublisher publisher;
    @Autowired
    private ObjectMapper objectMapper;

    private String buildPrompt(Question question) {
        return String.format("""
            Você irá ajudar na criação de variações de questões.
            Será enviado uma questão com a resposta certa e você deve criar variações nos níveis fácil, médio e difícil dessa questão.

            • Fácil (1): pergunta mais simples com 3-4 opções fáceis (incluindo a certa)
            • Médio (2): pergunta um pouco mais desafiadora com 4-5 opções medianas para acertar (incluindo a certa)
            • Difícil (3): pergunta com formulação mais elaborada ou técnica com 6 opções difíceis de acertar (incluindo a certa)

            Identifique as variações em dificuldade com os valores numérico acima definidos.

            Como resposta, apenas gere um json no seguinte formato (e não coloque dentro de bloco de código, retorne apenas com texto):
            [
                {
                    "difficulty": 1,
                    "question": "...",
                    "options": [
                    "...",
                    "...",
                    "..."
                    ]
                },
                {
                    "difficulty": 2,
                    "question": "...",
                    "options": [
                    "...",
                    "...",
                    "...",
                    "..."
                    ]
                },
                {
                    "difficulty": 3,
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
            
            Questão: %s
            Resposta certa: %s

                ATENÇÃO: Se atente ao contexto da pergunta e não fuja muito disso, tente mudar as respostas erradas com base no contexto, porém a resposta certa deve aparecer exatamente igual nas opções. Segue contexto: %s
            
            ATENÇÃO: Certifique-se que a resposta certa que estou te passando aparece EXATAMENTE igual nas opções de resposta de cada uma das questões. Podendo mudar apenas o seu índice na lista.

            ATENÇÃO: Não coloque prefixos de listagem nas respostas, ex: A. B. C.; I. II. III. ou 1. 2. 3.

            ATENÇÃO: Não coloque perguntas como opções de resposta, exceto que a resposta correta também seja uma pergunta.

            """, 
          question.getContexts() , question.getQuestion(), 
            question.getAnswer()
        );
    };

    public void generateRoomQuestionsVariants(
        Question question,   String roomCode,
        PedagogicalAgentCallback callback
    ) {
        String prompt = buildPrompt(question);

        System.out.println("Chegou aqui");
    publisher.emitGenerationStatus(roomCode, "Gerando");

    chatbot.request(prompt, response -> {
        publisher.emitGenerationStatus(roomCode, "Recebendo resposta da IA");

        response.ifPresentOrElse(output -> {
            publisher.emitGenerationStatus(roomCode, "Parseando JSON de variantes");
            String json = output.replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1").trim();
            try {
                List<QuestionVariant> variants = objectMapper.readValue(
                    json,
                    new TypeReference<List<QuestionVariant>>() {}
                );
                variants.forEach(v -> v.setOriginal(question));

                publisher.emitGenerationStatus(roomCode, "Gerado com sucesso");
                callback.accept(variants);
            } catch (IOException e) {
                publisher.emitGenerationStatus(roomCode, "Formato errado recebido");
                e.printStackTrace();
            }
        }, () -> {
            // resposta não presente
            publisher.emitGenerationStatus(roomCode, "Falha ao gerar");
        });
    });
}
};