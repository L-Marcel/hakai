package app.hakai.backend.repositories;

import java.util.LinkedList;

import org.springframework.stereotype.Repository;

import app.hakai.backend.transients.Room;
import lombok.Getter;
import lombok.Setter;

@Repository
public class RoomRepository {
    @Getter
    @Setter
    private LinkedList<Room> rooms = new LinkedList<Room>();

    public void addRoom(Room room) {
        rooms.add(room);
    };
};
