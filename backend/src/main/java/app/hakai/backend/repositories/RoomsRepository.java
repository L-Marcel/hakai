package app.hakai.backend.repositories;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Repository;

import app.hakai.backend.transients.Room;

@Repository
public class RoomsRepository {
    private LinkedList<Room> rooms = new LinkedList<Room>();

    public synchronized void add(Room room) {
        rooms.add(room);
    };

    public synchronized void remove(Room room) {
        rooms.remove(room);
    };

    private Optional<Room> find(Function<Room, Boolean> search) {
        for(int i = 0; i < rooms.size(); i++) {
            Room room = this.rooms.get(i);
            if(search.apply(room))
                return Optional.of(room);
        };

        return Optional.empty();
    };

    public Optional<Room> findByGame(UUID game) {
        return this.find((Room room) -> {
            return room.getGame().getUuid().equals(game);
        });
    };

    public Optional<Room> findByCode(String code) {
        return this.find((Room room) -> {
            return room.getCode().equals(code);
        });
    };

    public boolean existsByCode(String code) {
        return this.findByCode(code).isPresent();
    };

    public boolean existsByGame(UUID game) {
        return this.findByGame(game).isPresent();
    };
};
