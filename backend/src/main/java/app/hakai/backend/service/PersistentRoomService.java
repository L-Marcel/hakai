package app.hakai.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.PersistentRoom;
import app.hakai.backend.repository.PersistentRoomRepository;
import jakarta.transaction.Transactional;

@Service
public class PersistentRoomService {

    @Autowired
    private PersistentRoomRepository repository;

    /**
     * Ativa uma sala, marcando-a como aberta.
     * Se a sala não existir, uma nova é criada.
     *
     * @param gameId O UUID do jogo associado à sala.
     */
    @Transactional
    public void activateRoom(UUID gameId) {
        PersistentRoom pRoom = repository.findById(gameId).orElseGet(() -> {
            PersistentRoom newRoom = new PersistentRoom();
            newRoom.setGameId(gameId);
            return newRoom;
        });
        pRoom.setIsOpen(true);
        repository.save(pRoom);
    }

    /**
     * Desativa uma sala, marcando-a como fechada.
     *
     * @param gameId O UUID do jogo associado à sala.
     */
    @Transactional
    public void deactivateRoom(UUID gameId) {
        repository.findById(gameId).ifPresent(pRoom -> {
            pRoom.setIsOpen(false);
            repository.save(pRoom);
        });
    }
}
