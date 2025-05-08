package app.hakai.backend.services;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.errors.GameRoomAlreadyExists;
import app.hakai.backend.errors.ParticipantAlreadyInRoom;
import app.hakai.backend.errors.RoomNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.RoomsRepository;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;

@Service
public class RoomService {
    @Autowired
    private RoomsRepository repository;

    private String generateCode(int size) {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        };

        return builder.toString();
    };

    public synchronized Room getRoom(String code) throws RoomNotFound {
        return repository.findByCode(code).orElseThrow(
            () -> new RoomNotFound()
        );
    };

    private boolean isParticipantAlreadyInRoom(Room room, Participant candidate) {
        for(Participant participant : room.getParticipants()) {
            String nickname = participant.getNickname().toUpperCase();
            String candidateNickname = candidate.getNickname().toUpperCase();
            boolean nicknameAlreadyInUse = nickname.equals(candidateNickname);
            boolean idAlreadyInUse = participant.getUuid().equals(candidate.getUuid());
            boolean userAlreadyInUse = false;
            Optional<User> user = participant.getUser();
            Optional<User> candidateUser = candidate.getUser();
            if(user.isPresent() && candidateUser.isPresent()) {
                userAlreadyInUse = user.get().getEmail().equals(candidateUser.get().getEmail());
            };

            if(nicknameAlreadyInUse || idAlreadyInUse || userAlreadyInUse) 
                return true;
        };

        return false;
    };

    public synchronized Room createRoom(Game game) throws GameNotFound {
        if(game == null) throw new GameNotFound();
        else if(repository.existsByGame(game.getUuid())) 
            throw new GameRoomAlreadyExists();

        String code = this.generateCode(6);
        while(repository.existsByCode(code)) 
            code.concat(this.generateCode(2));
        
        Room room = new Room(
            code, 
            game, 
            new LinkedList<Participant>(), 
            false
        );

        repository.add(room);
        return room;
    };

    public synchronized void joinRoom(
        Room room, 
        Participant participant
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        boolean alreadyInRoom = this.isParticipantAlreadyInRoom(room, participant);
        if(alreadyInRoom) throw new ParticipantAlreadyInRoom();
        room.getParticipants().add(participant);
    };

    public synchronized void closeRoom(
        Room room
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        repository.remove(room);
    };
};
