package app.hakai.backend.transients;

import java.util.Optional;
import java.util.UUID;

import app.hakai.backend.models.Difficult;
import app.hakai.backend.models.User;
import lombok.Getter;

@Getter 
public class Participant {
    private UUID uuid = UUID.randomUUID();
    private Room room;
    private Optional<User> user;
    private String nickname;
    private Difficult currentDifficult = Difficult.NORMAL;
    
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int score = 0;

    public Participant(String nickname, Room room) {
        this.nickname = nickname;
        this.room = room;
        this.user = Optional.empty();
    };

    public Participant(String nickname, Room room, User user) {
        this.nickname = nickname;
        this.room = room;
        this.user = Optional.of(user);
    };

    public void incrementScore() {
        switch (this.currentDifficult) {
            case Difficult.EASY:
                this.score += 100;
                break;
            case Difficult.NORMAL:
                this.score += 200;
                break;
            case Difficult.HARD:
                this.score += 300;
                break;
            default:
                break;
        }
    };

    public void incrementCorrectAnswers() {
        this.correctAnswers += 1;
    };

    public void incrementWrongAnswers() {
        this.wrongAnswers += 1;
    };
};
