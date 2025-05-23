package app.hakai.backend.dtos;

import java.util.UUID;

import app.hakai.backend.models.Difficulty;
import app.hakai.backend.transients.Participant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    private UUID uuid;
    private UUID user;
    private String room;
    private String nickname;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private int score = 0;

    public ParticipantResponse(Participant participant) {
        this.uuid = participant.getUuid();
        this.nickname = participant.getNickname();
        this.score = participant.getScore();
        this.room = participant.getRoom().getCode();
        this.currentDifficulty = participant.getCurrentDifficulty();
        if(participant.getUser().isPresent()) {
            this.user = participant.getUser().get().getUuid();
        };
    };
};
