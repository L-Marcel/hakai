package app.hakai.backend.controllers;

import java.util.UUID;

import org.kahai.framework.annotations.RequireAuth;
import org.kahai.framework.dtos.request.CreateRoomRequestBody;
import org.kahai.framework.dtos.request.JoinRoomRequestBody;
import org.kahai.framework.dtos.response.ParticipantResponse;
import org.kahai.framework.dtos.response.RoomResponse;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.services.AccessControlService;
import org.kahai.framework.services.GameService;
import org.kahai.framework.services.ParticipantService;
import org.kahai.framework.services.RoomService;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.Room;
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

import app.hakai.backend.strategies.RoomEventStrategyKahoot;
import jakarta.annotation.PostConstruct;

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

    
    @PostConstruct
    public void setUpStrategies() {
        this.roomService.setRoomStrategy(
            new RoomEventStrategyKahoot()
        );
    };

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