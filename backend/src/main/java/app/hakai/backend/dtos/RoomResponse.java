package app.hakai.backend.dtos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.hakai.backend.transients.Room;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {
    private String code;
    private UUID game;
    private UUID owner;
    private List<ParticipantResponse> participants;

    public RoomResponse(Room room) {
        this.code = room.getCode();
        this.game = room.getGame().getUuid();
        this.owner = room.getGame().getOwner().getUuid();
        this.participants = room.getParticipants()
            .stream()
            .map(ParticipantResponse::new)
            .collect(Collectors.toList());
    };
};
