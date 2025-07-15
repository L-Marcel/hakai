package app.hakai.backend.service;

import java.util.List;

import org.kahai.framework.errors.RoomNotFound;
import org.kahai.framework.models.Game;
import org.kahai.framework.services.GameService;
import org.kahai.framework.services.RoomService;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.PersistentRoom;

@Service
public class RoomSchedulerService {

    @Autowired
    private PersistentRoomService persistentRoomService;
    @Autowired
    private GameService gameService;
    @Autowired
    private RoomService roomService;

    @Scheduled(fixedRate = 60000)
    public void closeExpiredRooms() {
        List<PersistentRoom> expiredRooms = persistentRoomService.findExpiredOpenRooms();

        for (PersistentRoom pRoom : expiredRooms) {
            System.out.println("SCHEDULER: A sala do jogo " + pRoom.getGameId() + " expirou. Fechando...");

            persistentRoomService.deactivateRoom(pRoom.getGameId());

            try {
                Game game = gameService.findGameById(pRoom.getGameId());
                Room roomInMemory = roomService.findRoomByGame(game);
                roomService.closeRoom(roomInMemory);

                System.out.println(
                        "SCHEDULER DE BACKUP: Sala do jogo " + pRoom.getGameId() + " também foi fechada na memória.");

            } catch (RoomNotFound e) {
                System.out.println("SCHEDULER DE BACKUP: Sala do jogo " + pRoom.getGameId()
                        + " não encontrada na memória (OK), status foi corrigido no banco de dados.");
            } catch (Exception e) {
                System.err.println("SCHEDULER DE BACKUP: Erro inesperado ao fechar sala do jogo " + pRoom.getGameId()
                        + ": " + e.getMessage());
            }
        }
    }
}
