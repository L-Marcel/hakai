package app.hakai.backend.services;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.errors.UserRoomAlreadyExists;
import app.hakai.backend.events.RoomEventPublisher;
import app.hakai.backend.errors.ParticipantAlreadyInRoom;
import app.hakai.backend.errors.RoomNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.RoomRepository;
import app.hakai.backend.transients.Room;

@Service
public class RoomService {
    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    private String generateCode(int size) {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        };

        return builder.toString();
    };

    public Room findRoomByCode(
        String code
    ) throws RoomNotFound {
        return repository.findByCode(code)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByGame(
        Game game
    ) throws RoomNotFound {
        return repository.findByGame(game)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByUser(
        User user
    ) throws RoomNotFound {
        return repository.findByUser(user)
            .orElseThrow(RoomNotFound::new);
    };

    public Room createRoom(
        Game game
    ) throws GameNotFound {
        synchronized(repository) {
            if(repository.existsByUser(game.getOwner())) 
            throw new UserRoomAlreadyExists();

            String code = this.generateCode(6);
            while(repository.existsByCode(code)) 
                code = code.concat(this.generateCode(2));
            
            Room room = new Room(
                code, 
                game
            );

            repository.add(room);
            return room;
        }
    };

    public void closeRoom(
        Room room
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        repository.remove(room);
        roomEventPublisher.emitRoomClosed(room);
    };
};
