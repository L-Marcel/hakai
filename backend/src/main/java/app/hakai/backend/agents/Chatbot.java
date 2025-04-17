package app.hakai.backend.agents;

public interface Chatbot {
    void request(String prompt, ChatbotCallback callback);
};
