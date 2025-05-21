package app.hakai.backend.transients;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import app.hakai.backend.models.Game;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private String code;
    private Game game;
    private List<Participant> participants;

    public Room(String code, Game game) {
        this.code = code;
        this.game = game;
        this.participants = Collections.synchronizedList(
            new LinkedList<Participant>()
        );
    };
};