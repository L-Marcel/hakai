package app.hakai.backend.repositories;

import java.util.LinkedList;

import org.springframework.stereotype.Repository;

import app.hakai.backend.transients.Room;

@Repository
public class RoomsRepository {
    private LinkedList<Room> rooms = new LinkedList<Room>();

    public void add(Room room) {
        rooms.add(room);
    };

    public boolean existsByCode(String code) {
        for(int i = 0; i < rooms.size(); i++) {
            Room room = this.rooms.get(i);
            if(room.getCode().equals(code)) {
                return true;
            };
        };

        return false;
    };
};
