package app.hakai.backend.transients;

import java.util.Optional;
import java.util.UUID;

import app.hakai.backend.models.Difficulty;
import app.hakai.backend.models.User;
import lombok.Getter;

@Getter 
public class Participant {
    private UUID uuid = UUID.randomUUID();
    private Room room;
    private Optional<User> user;
    private String nickname;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    
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
        switch (this.currentDifficulty) {
            case Difficulty.EASY:
                this.score += 100;
                break;
            case Difficulty.NORMAL:
                this.score += 200;
                break;
            case Difficulty.HARD:
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

    public Difficulty setNetxDifficulty(Boolean isCorrect){
        Difficulty[] difficulties = Difficulty.values();
        int index = currentDifficulty.ordinal();

        if (isCorrect && index < difficulties.length - 1) {
            return currentDifficulty = difficulties[index + 1];
        } 
        else if (!isCorrect && index > 0) {
            return currentDifficulty = difficulties[index - 1];
        }

        return currentDifficulty;
    }
};
