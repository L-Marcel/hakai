package app.hakai.backend.agents;

public class AITest {
    public static void main(String[] args) throws InterruptedException {
        AIAgent ia = new AIAgent();
        ia.request("Me diga um fato curioso sobre o espaço!");
        Thread.sleep(5000);
    };
};
