package app.hakai.backend.models;

import java.util.LinkedList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Game {
    @Getter
    @Setter
    private UUID uuid;

    @Getter
    @Setter
    private LinkedList<Question> questions = new LinkedList<Question>();
};
