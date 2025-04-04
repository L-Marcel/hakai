package app.hakai.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.Game;
import app.hakai.backend.repositories.RoomRepository;
import app.hakai.backend.transients.Room;

@Service
public class RoomService {
    @Autowired
    private RoomRepository repository;

    public Room createRoom(Game game) {
        Room room = new Room();
        room.setCode(null);
        room.setGame(game);
        repository.addRoom(room);
        return room;
    };
};
