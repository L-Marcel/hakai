package app.hakai.backend.dtos;

import java.util.UUID;

import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;
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
    private RoomResponse room;

    public JoinRoomResponse(Participant participant, Room room) {
        this.uuid = participant.getUuid();
        this.nickname = participant.getNickname();
        this.currentDifficult = participant.getCurrentDifficult();
        this.correctAnswers = participant.getCorrectAnswers();
        this.wrongAnswers = participant.getWrongAnswers();
        this.score = participant.getScore();
        this.room = new RoomResponse(room);
    };
};
