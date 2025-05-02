package app.hakai.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import app.hakai.backend.dtos.CreateRoomResponse;
import app.hakai.backend.dtos.JoinRoomResponse;
import app.hakai.backend.models.Game;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate simp;

    @PostMapping("/create")
    public ResponseEntity<CreateRoomResponse> create(
        @RequestBody Game game
    ) {
        game = gameService.getGame(game.getUuid());
        Room createdRoom = roomService.createRoom(game);
        CreateRoomResponse response = new CreateRoomResponse(createdRoom);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };

    @GetMapping("/{code}")
    public ResponseEntity<String> get(
        @PathVariable String code
    ){
        roomService.getRoom(code);
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .body("Sala encontrada!");
    };

    @PostMapping("/{code}/join")
    public ResponseEntity<JoinRoomResponse> join(
        @PathVariable String code,
        Participant participant
    ) {
        roomService.joinRoom(code, participant);
        simp.convertAndSend("/" + code + "/users/entered");
        JoinRoomResponse response = new JoinRoomResponse(participant);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };
};