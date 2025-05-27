package app.hakai.backend.events;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import app.hakai.backend.dtos.response.QuestionVariantResponse;
import app.hakai.backend.dtos.response.RoomResponse;
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

    public void emitVariantsGenerated(
        Room room, 
        List<QuestionVariant> variants
    ) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + 
            "/" + room.getGame().getOwner().getUuid().toString() + 
            "/variants",
            variants.stream().map(
                (variant) -> new QuestionVariantResponse(
                    variant, 
                    true
                )
            )
        );
    };

    public void emitVariantIntended(
        Room room, 
        UUID participant, 
        QuestionVariant variant
    ) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + 
            "/participants/" + participant + "/question",
            new QuestionVariantResponse(
                variant, 
                false
            )
        );
    };
    
    public void emitGenerationStatus(
        Room room, 
        String status
    ) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/status",
            status
        );
    };
};
