package app.hakai.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import app.hakai.backend.dtos.QuestionVariantResponse;
import app.hakai.backend.dtos.RoomResponse;
import app.hakai.backend.transients.QuestionVariant;
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

    public void closeRoomToAll(Room room) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/closed",
            "Sala fechada!"
        );
    };

    public void sendRoomToParticipant(Room room, UUID participant) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/" + participant.toString() + "/entered", 
            new RoomResponse(room)
        );
    };


    public void sendVariantsToOwner(Room room, List<QuestionVariant> list) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/" + room.getGame().getOwner().getUuid().toString(),
            list.stream().map((QuestionVariant question) -> new QuestionVariantResponse(question, true))
        );
    }
};
