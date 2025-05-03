package app.hakai.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import app.hakai.backend.dtos.RoomResponse;
import app.hakai.backend.transients.Room;

@Service
public class MessagingService {
    @Autowired
    private SimpMessagingTemplate simp;

    public void sendRoomToAll(Room room) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/participants/entered", 
            new RoomResponse(room)
        );
    };

    public void sendRoomToParticipant(Room room, UUID participant) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/" + participant.toString() + "/entered", 
            new RoomResponse(room)
        );
    };
};
