package app.hakai.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.anotations.RequireAuth;
import app.hakai.backend.dtos.GameResponse;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;
import app.hakai.backend.services.AccessControlService;
import app.hakai.backend.services.GameService;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private AccessControlService accessControlService;

    @RequireAuth
    @GetMapping("/{uuid}")
    public ResponseEntity<GameResponse> get(
        @PathVariable(required = false) UUID uuid,
        @AuthenticationPrincipal User user
    ) {
        Game game = gameService.findGameById(uuid);
        accessControlService.checkGameOwnership(user, game);
        
        GameResponse response = new GameResponse(game);

        return ResponseEntity
            .ok()
            .body(response);
    };

    @GetMapping
    @RequireAuth
    public ResponseEntity<List<GameResponse>> getGamesToUser(
        @AuthenticationPrincipal User user
    ) {
        List<Game> games = gameService.findGamesByUser(user.getUuid());
        List<GameResponse> response = GameResponse.mapFromList(games);
        
        return ResponseEntity.ok(response);
    };
};