package app.hakai.backend.controllers;

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
    public ResponseEntity<Game> get(@PathVariable String uuid) throws GameNotFound{
        try {
            UUID validUuid = UUID.fromString(uuid);  
            Game game = service.getGame(validUuid).orElseThrow(() -> new GameNotFound(validUuid.toString()));
            
            return ResponseEntity.ok().body(game);
        } 
        catch (IllegalArgumentException e) {
            throw new GameNotFound(uuid);
        }
    }
}
