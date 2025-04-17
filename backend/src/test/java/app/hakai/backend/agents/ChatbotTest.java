package app.hakai.backend.agents;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatbotTest {
    @Autowired
    private Chatbot chatbot;

    @Test
    void shouldSendResponse(){
        ChatbotCallback callback = Mockito.mock(ChatbotCallback.class);

        chatbot.request(
            "Você faz parte de um teste unitário de conexão.\n" + 
            "Responda em menos de 20 segundos EXATAMENTE: Funciona!", 
            callback
        );
        
        Awaitility
            .await()
            .atMost(60, TimeUnit.SECONDS)
            .untilAsserted(
                () -> Mockito.verify(callback).accept(
                    Optional.of("Funciona!")
                )
            );
    };
};
