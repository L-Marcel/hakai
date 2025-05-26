package app.hakai.backend.agents;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AIChatbot implements Chatbot {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private KeySource keySource;

    public AIChatbot() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    };

    @Override
    public void request(String prompt, ChatbotCallback callback) {
        try {
            Map<String, Object> requestBody = Map.of(
                "model", "nousresearch/deephermes-3-mistral-24b-preview:free",
                "messages", List.of(
                    Map.of(
                        "role", "user", 
                        "content", prompt
                    )
                )
            );

            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.keySource.getKey())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    System.out.println("ABCD2");
                    try {
                        JsonNode root = objectMapper.readTree(responseBody);
                            String responseText = root.path("choices").get(0).path("message").path("content").asText();
                        System.out.println("Resposta: " + responseText);
                            callback.accept(Optional.of(responseText));
                        
                    } catch (Exception e) {
                        System.out.println("Erro ao processar a resposta: " + e.getMessage());
                    };
                })
                .exceptionally(e -> {
                    callback.accept(Optional.empty());
                    return null;
                });
        } catch (Exception e) {
            callback.accept(Optional.empty());
        };
    };
};