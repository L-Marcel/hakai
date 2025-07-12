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
        // Usa o serviço de persistência para marcar a sala como OPEN com um tempo de
        // expiração.
        persistenceService.activateRoom(room.getGame().getUuid(), (int) room.getDuration().toMinutes());
    }

    /**
     * Este método é chamado pelo RoomService.closeRoom()
     */
    @Override
    public void onClose(Room room) {
        System.out.println("ESTRATÉGIA PROVAI: onClose foi chamado para o jogo " + room.getGame().getUuid());
        // Usa o serviço de persistência para marcar a sala como CLOSED.
        persistenceService.deactivateRoom(room.getGame().getUuid());
    }

    /**
     * Este método é chamado pelo RoomService.roomTimeExceeded()
     * quando o timer interno dele expira.
     */
    @Override
    public void onDurationExceeded(Room room) {
        System.out.println("ESTRATÉGIA PROVAI: onDurationExceeded foi chamado para o jogo " + room.getGame().getUuid());
        // A lógica é a mesma de fechar a sala: marcar como CLOSED no banco.
        persistenceService.deactivateRoom(room.getGame().getUuid());
    }
}