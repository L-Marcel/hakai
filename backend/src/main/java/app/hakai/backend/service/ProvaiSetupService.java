package app.hakai.backend.service;

import java.util.Optional;

import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.services.RoomService;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import app.hakai.backend.repository.PersistentRoomRepository;
import app.hakai.backend.enums.RoomStatus;
import app.hakai.backend.models.PersistentRoom;
import app.hakai.backend.repository.PersistentRoomRepository;
import org.kahai.framework.models.Game;
import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.services.RoomService;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProvaiSetupService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PersistentRoomRepository persistentRoomRepository;
    @Autowired
    private RoomRepository transientRoomRepository;

    @Autowired
    @Qualifier("provaiRoomEventStrategy")
    private RoomEventStrategy provaiStrategy;

    public Room setupRoom(Game game, Duration duration) {
        Optional<PersistentRoom> persistentRoomOpt = persistentRoomRepository.findById(game.getUuid());

        if (persistentRoomOpt.isPresent() && persistentRoomOpt.get().getStatus() == RoomStatus.CLOSED) {
            System.out.println("SETUP PROVAI: Reabrindo sala persistente para o jogo " + game.getUuid());

            PersistentRoom pRoom = persistentRoomOpt.get();
            pRoom.setStatus(RoomStatus.OPEN);
            pRoom.setClosingTime(LocalDateTime.now().plus(duration));
            persistentRoomRepository.save(pRoom);

            Room roomInMemory = new Room(generateNewCode(), game, duration);
            transientRoomRepository.add(roomInMemory);

            roomService.startRoomTimer(roomInMemory);
            return roomInMemory;
        }
        System.out.println("SETUP PROVAI: Criando uma nova sala para o jogo " + game.getUuid());
        Room newRoom = roomService.createRoom(game, duration);
        provaiStrategy.onStart(newRoom);
        roomService.startRoomTimer(newRoom);
        return newRoom;
    }

    private String generateNewCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }
}