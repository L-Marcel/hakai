package app.hakai.backend.transients;

import java.util.Optional;
import java.util.UUID;

import app.hakai.backend.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Participant {
    private UUID uuid = UUID.randomUUID();
    private Optional<User> user;
    private String nickname;
    private int currentDifficult = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int score = 0;

    public Participant(String nickname) {
        this.nickname = nickname;
        this.user = Optional.empty();
    };
};
