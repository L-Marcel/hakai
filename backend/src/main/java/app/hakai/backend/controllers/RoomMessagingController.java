package app.hakai.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import app.hakai.backend.services.MessagingService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Room;

@Controller
@MessageMapping
public class RoomMessagingController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private MessagingService messagingService;

    @MessageMapping("/{code}/{participant}")
    public void participant(
        @DestinationVariable String code, 
        @DestinationVariable UUID participant
    ) {
        Room room = roomService.findRoomByCode(code);
        messagingService.sendRoomToParticipant(room, participant);
    };
};