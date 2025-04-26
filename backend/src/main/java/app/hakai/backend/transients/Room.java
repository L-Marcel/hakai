package app.hakai.backend.transients;

import java.util.List;

import app.hakai.backend.models.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Room {
    private String code;
    private Game game;
    private List<Participant> participants;
    private boolean ready;
};