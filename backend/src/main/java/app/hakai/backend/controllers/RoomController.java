package app.hakai.backend.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import app.hakai.backend.models.Game;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Room;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<Room> create(@RequestBody Game game) {
        Room createdRoom = roomService.createRoom(game);
        return ResponseEntity.ok().body(createdRoom);
    };
};