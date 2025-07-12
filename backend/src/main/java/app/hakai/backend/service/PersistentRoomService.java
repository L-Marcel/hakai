package app.hakai.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.enums.RoomStatus;
import app.hakai.backend.models.PersistentRoom;
import app.hakai.backend.repository.PersistentRoomRepository;
import jakarta.transaction.Transactional;

@Service
public class PersistentRoomService {

    @Autowired
    private PersistentRoomRepository repository;

    @Transactional
    public void activateRoom(UUID gameId, int durationInMinutes) {
        PersistentRoom pRoom = repository.findById(gameId).orElseGet(() -> {
            PersistentRoom newRoom = new PersistentRoom();
            newRoom.setGameId(gameId);
            return newRoom;
        });
        pRoom.setStatus(RoomStatus.OPEN);
        pRoom.setClosingTime(LocalDateTime.now().plusMinutes(durationInMinutes));

        repository.save(pRoom);
    }

    @Transactional
    public void deactivateRoom(UUID gameId) {
        repository.findById(gameId).ifPresent(pRoom -> {
            pRoom.setStatus(RoomStatus.CLOSED);
            repository.save(pRoom);
        });
    }

    public List<PersistentRoom> findExpiredOpenRooms() {
        return repository.findByStatusAndClosingTimeBefore(RoomStatus.OPEN, LocalDateTime.now());
    }
}