package app.hakai.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import app.hakai.backend.models.Game;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Room;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<Room> create(
        @RequestBody Game game
    ) {
        game = gameService.getGame(game.getUuid());
        Room createdRoom = roomService.createRoom(game);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdRoom);
    };
};