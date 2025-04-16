package app.hakai.backend.AI;

public class TestIA {
    public static void main(String[] args) throws InterruptedException {
        AIAgent ia = new AIAgent();
        ia.request("Me diga um fato curioso sobre o espaço!");
        Thread.sleep(5000);
    }
}
