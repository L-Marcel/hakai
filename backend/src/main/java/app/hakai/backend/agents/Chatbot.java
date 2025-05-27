package app.hakai.backend.agents;

public interface Chatbot {
    void request(
        String systemPrompt, 
        String prompt, 
        ChatbotCallback callback
    );
};
