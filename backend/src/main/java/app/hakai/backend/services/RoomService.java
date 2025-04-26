package app.hakai.backend.services;

import java.util.LinkedList;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.GameNotFound;
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
        return Integer.toString(
            RandomUtils.secure().hashCode()
        ).substring(0, size);
    };

    private Room findRoomByCode(String code) throws RoomNotFound {
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
            User user = participant.getUser();
            User candidateUser = candidate.getUser();
            if(user != null && candidateUser != null) {
                userAlreadyInUse = user.getEmail().equals(candidateUser.getEmail());
            };

            if(nicknameAlreadyInUse || idAlreadyInUse || userAlreadyInUse) 
                return true;
        };

        return false;
    };

    public synchronized Room createRoom(Game game) throws GameNotFound {
        if(game == null) throw new GameNotFound();

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

    public void joinRoom(
        String code, 
        Participant participant
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        Room room = this.findRoomByCode(code);
        boolean alreadyInRoom = this.isParticipantAlreadyInRoom(room, participant);
        if(alreadyInRoom) throw new ParticipantAlreadyInRoom();
        room.getParticipants().add(participant);
    };
};
