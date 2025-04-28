package app.hakai.backend.dtos;

import app.hakai.backend.transients.Room;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomResponse {
    private String code;

    public CreateRoomResponse(Room room) {
        this.code = room.getCode();
    };
};
