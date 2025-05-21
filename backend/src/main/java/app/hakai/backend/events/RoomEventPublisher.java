package app.hakai.backend.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import app.hakai.backend.dtos.QuestionVariantResponse;
import app.hakai.backend.dtos.RoomResponse;
import app.hakai.backend.transients.QuestionVariant;
import app.hakai.backend.transients.Room;

@Component
public class RoomEventPublisher {
    @Autowired
    private SimpMessagingTemplate simp;

    public void emitRoomUpdated(Room room) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/updated", 
            new RoomResponse(room)
        );
    };

    public void emitRoomClosed(Room room) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/closed",
            "Sala fechada!"
        );
    };

    public void emitVariantsGenerated(Room room, List<QuestionVariant> list) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + 
            "/" + room.getGame().getOwner().getUuid().toString() + 
            "/variants",
            list.stream().map(
                (QuestionVariant question) -> new QuestionVariantResponse(
                    question, 
                    true
                )
            )
        );
    };
};
