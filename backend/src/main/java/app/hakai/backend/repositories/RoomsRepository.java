package app.hakai.backend.repositories;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.hakai.backend.transients.Room;

@Repository
public class RoomsRepository {
    private LinkedList<Room> rooms = new LinkedList<Room>();

    public synchronized void add(Room room) {
        rooms.add(room);
    };

    public Optional<Room> findByCode(String code) {
        for(int i = 0; i < rooms.size(); i++) {
            Room room = this.rooms.get(i);
            if(room.getCode().equals(code))
                return Optional.of(room);
        };

        return Optional.empty();
    };

    public boolean existsByCode(String code) {
        return this.findByCode(code).isPresent();
    };
};
