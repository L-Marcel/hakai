package app.hakai.backend.AI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AIAgent {
    private static final String API_KEY = "COLOQUE SUA CHAVE AQUI";
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AIAgent() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public void request(String prompt) {
        try {
            System.out.println("Chegou requisição: " + prompt);
            Map<String, Object> requestBody = Map.of(
                    "model", "openai/gpt-3.5-turbo",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)));

            String json = objectMapper.writeValueAsString(requestBody);

            // Monta e envia requisição
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // Envia e imprime resposta
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> {
                        try {
                            JsonNode root = objectMapper.readTree(responseBody);
                            String responseText = root.path("choices").get(0).path("message").path("content").asText();
                            System.out.println("Resposta da IA:");
                            System.out.println(responseText);
                        } catch (Exception e) {
                            System.out.println("Erro ao processar a resposta: " + e.getMessage());
                        }
                    })
                    .exceptionally(e -> {
                        System.out.println("Erro ao enviar requisição: " + e.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            System.out.println("Erro ao preparar requisição: " + e.getMessage());
        }
    }
}