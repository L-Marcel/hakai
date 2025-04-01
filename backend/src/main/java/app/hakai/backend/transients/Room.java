package app.hakai.backend.transients;

import app.hakai.backend.models.Game;
import lombok.Getter;
import lombok.Setter;

public class Room {
    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private Game game;

    @Getter
    @Setter
    private boolean ready = false;
};
