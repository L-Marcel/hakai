package app.hakai.backend.transients;

import app.hakai.backend.models.Game;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private String code;
    private Game game;
    private boolean ready = false;
};