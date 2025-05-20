package app.hakai.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.dtos.RoomResponse;
import app.hakai.backend.anotations.RequireAuth;
import app.hakai.backend.dtos.CreateRoomRequestBody;
import app.hakai.backend.dtos.JoinRoomRequestBody;
import app.hakai.backend.dtos.ParticipantResponse;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;
import app.hakai.backend.services.AccessControlService;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.MessagingService;
import app.hakai.backend.services.ParticipantService;
import app.hakai.backend.services.QuestionVariantsService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private QuestionVariantsService questionVariantsService;

    @Autowired
    private AccessControlService accessControlService;

    @RequireAuth
    @PostMapping("/create")
    public ResponseEntity<RoomResponse> create(
        @RequestBody CreateRoomRequestBody body,
        @AuthenticationPrincipal User user
    ) {
        Game game = gameService.findGameById(body.getGame());
        accessControlService.checkGameOwnership(user, game);

        Room createdRoom = roomService.createRoom(game);
        RoomResponse response = new RoomResponse(createdRoom);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };

    @RequireAuth
    @PostMapping("/close")
    public ResponseEntity<Void> close(
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user.getUuid());
        
        roomService.closeRoom(room);
        messagingService.closeRoomToAll(room);

        return ResponseEntity
            .ok()
            .build();
    };

    @RequireAuth
    @PostMapping("/questions/{uuid}/generate")
    public ResponseEntity<Void> requestToGenerate(
        @PathVariable UUID uuid,
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user.getUuid());
        questionVariantsService.startVariantsGeneration(uuid, room);

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .build();
    };

    @GetMapping("/{code}")
    public ResponseEntity<RoomResponse> get(
        @PathVariable String code
    ){
        Room room = roomService.findRoomByCode(code);
        RoomResponse response = new RoomResponse(room);

        return ResponseEntity
            .ok()
            .body(response);
    };

    @PostMapping("/{code}/join")
    public ResponseEntity<ParticipantResponse> join(
        @PathVariable String code,
        @RequestBody JoinRoomRequestBody body,
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByCode(code);
        Participant participant = participantService.createParticipant(
            body.getNickname(), 
            user
        );

        roomService.joinRoom(room, participant);
        messagingService.sendRoomToAll(room);
        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };
};