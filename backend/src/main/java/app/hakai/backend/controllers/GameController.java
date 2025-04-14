package app.hakai.backend.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.services.GameService;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameService service;

    @GetMapping("/{uuid}")
    public ResponseEntity<Game> get(@PathVariable UUID uuid) throws GameNotFound{
        Game game = service.getGame(uuid).orElseThrow(() -> new GameNotFound(uuid.toString()));

        return ResponseEntity.ok().body(game);
    }
}
