package app.hakai.backend.dtos;

import java.util.UUID;

import app.hakai.backend.transients.Participant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRoomResponse {
    private UUID uuid;
    private String nickname;
    private int currentDifficult;
    private int correctAnswers;
    private int wrongAnswers;
    private int score;

    public JoinRoomResponse(Participant participant) {
        this.uuid = participant.getUuid();
        this.nickname = participant.getNickname();
        this.currentDifficult = participant.getCurrentDifficult();
        this.correctAnswers = participant.getCorrectAnswers();
        this.wrongAnswers = participant.getWrongAnswers();
        this.score = participant.getScore();
    };
};
