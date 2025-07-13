package app.hakai.backend.service;

import java.util.List;

import org.kahai.framework.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.PersistentRoom;

@Service
public class RoomSchedulerService {

    @Autowired
    private PersistentRoomService persistentRoomService;

    @Autowired
    private RoomService roomService;

    @Scheduled(fixedRate = 60000)
    public void closeExpiredRooms() {
        List<PersistentRoom> expiredRooms = persistentRoomService.findExpiredOpenRooms();

        for (PersistentRoom pRoom : expiredRooms) {
            System.out.println("SCHEDULER: A sala do jogo " + pRoom.getGameId() + " expirou. Fechando...");

            persistentRoomService.deactivateRoom(pRoom.getGameId());

            try {
                // Aqui você precisaria de uma forma de obter o objeto Game a partir do gameId
                // para então encontrar a Room. Isso pode exigir uma alteração no RoomService.
                // Exemplo simplificado:
                // Game game = gameService.findById(pRoom.getGameId());
                // Room roomInMemory = roomService.findRoomByGame(game);
                // roomService.closeRoom(roomInMemory);
            } catch (Exception e) {
                // Sala não estava na memória, o que é esperado se o servidor reiniciou.
                // O importante é que o status no banco de dados foi atualizado.
                System.out.println("SCHEDULER: Sala do jogo " + pRoom.getGameId()
                        + " não encontrada na memória, mas status foi atualizado no BD.");
            }
        }
    }
}
