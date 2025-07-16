package app.hakai.backend.strategies;

import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.hakai.backend.service.PersistentRoomService;

@Component
public class ProvaiRoomEventStrategy implements RoomEventStrategy {
    @Autowired
    private PersistentRoomService persistenceService;

    @Override
    public void onStart(Room room) {
        System.out.println("ESTRATÉGIA PROVAI: onStart foi chamado para o jogo " + room.getGame().getUuid());

        persistenceService.activateRoom(room.getGame().getUuid());
    }

    @Override
    public void onClose(Room room) {
        System.out.println("ESTRATÉGIA PROVAI: onClose foi chamado para o jogo " + room.getGame().getUuid());
        persistenceService.deactivateRoom(room.getGame().getUuid());
    }

    @Override
    public void onDurationExceeded(Room room) {
        System.out.println("ESTRATÉGIA PROVAI: onDurationExceeded foi chamado para o jogo " + room.getGame().getUuid());
        persistenceService.deactivateRoom(room.getGame().getUuid());
    }
}
