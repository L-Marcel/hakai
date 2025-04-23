package app.hakai.backend.agents;

import app.hakai.backend.models.Question;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.RoomService;

public class PedagogicalAgent {
    private String prompt;
    //private IAagent agent;
    private RoomService roomService;
    private GameService gameService;

    public PedagogicalAgent(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }    

    private Question[] convertOutputTQuestionVariants(String output) {
        return null;
    }

    public void generateRoomQuestionsVariants(String code) {

    }
}
