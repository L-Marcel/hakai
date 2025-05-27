package app.hakai.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.dtos.request.CreateRoomRequestBody;
import app.hakai.backend.dtos.request.JoinRoomRequestBody;
import app.hakai.backend.dtos.response.ParticipantResponse;
import app.hakai.backend.dtos.response.RoomResponse;
import app.hakai.backend.annotations.RequireAuth;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;
import app.hakai.backend.services.AccessControlService;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.ParticipantService;
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
    private AccessControlService accessControlService;

    @RequireAuth
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
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
    @DeleteMapping
    public ResponseEntity<Void> closeRoom(
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user);
        participantService.removeAllByRoom(room);
        roomService.closeRoom(room);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };

    @RequireAuth
    @DeleteMapping("/participants/{uuid}")
    public ResponseEntity<Void> kickFromRoom(
        @PathVariable UUID uuid,
        @AuthenticationPrincipal User user
    ) {
        Participant participant = participantService.findParticipantByUuid(uuid);
        accessControlService.checkRoomOwnership(user, participant.getRoom());
        participantService.removeParticipant(participant);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };

    @RequireAuth
    @GetMapping
    public ResponseEntity<RoomResponse> findRoomByUser(
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user);
        RoomResponse response = new RoomResponse(room);

        return ResponseEntity.ok(response);
    };

    @GetMapping("/{code}")
    public ResponseEntity<RoomResponse> findRoomByCode(
        @PathVariable String code
    ) {
        Room room = roomService.findRoomByCode(code);
        RoomResponse response = new RoomResponse(room);

        return ResponseEntity.ok(response);
    };
    
    @PostMapping("/{code}/join")
    public ResponseEntity<ParticipantResponse> joinRoom(
        @PathVariable String code,
        @RequestBody JoinRoomRequestBody body,
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByCode(code);
        Participant participant = participantService.createParticipant(
            body, 
            room,
            user
        );

        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };
};