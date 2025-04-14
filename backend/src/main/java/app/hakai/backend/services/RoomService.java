package app.hakai.backend.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.repositories.RoomRepository;
import app.hakai.backend.transients.Room;

@Service
public class RoomService {
    @Autowired
    private RoomRepository repository;

    private String generateCode(int size) {
        return RandomStringUtils.secure().next(size);
    };

    public synchronized Room createRoom(Game game) throws GameNotFound {
        if(game == null) throw new GameNotFound(null);

        Room room = new Room();

        String code = this.generateCode(6);
        while(repository.existsByCode(code)) 
            code.concat(this.generateCode(2));
        
        room.setCode(code);
        room.setGame(game);
        repository.add(room);
        return room;
    };
};
