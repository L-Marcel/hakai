package app.hakai.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.annotations.RequireAuth;
import org.kahai.framework.dtos.request.GameRequest;
import org.kahai.framework.dtos.response.GameResponse;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.services.AccessControlService;
import org.kahai.framework.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private AccessControlService accessControlService;

    @RequireAuth
    @GetMapping("/{uuid}")
    public ResponseEntity<GameResponse> findGame(
        @PathVariable UUID uuid,
        @AuthenticationPrincipal User user
    ) {
        Game game = gameService.findGameById(uuid);
        accessControlService.checkGameOwnership(user, game);

        GameResponse response = new GameResponse(game);

        return ResponseEntity.ok(response);
    };

    @GetMapping
    @RequireAuth
    public ResponseEntity<List<GameResponse>> findGamesByUser(
        @AuthenticationPrincipal User user
    ) {
        List<Game> games = gameService.findGamesByUser(user);
        List<GameResponse> response = GameResponse.mapFromList(games);

        return ResponseEntity.ok(response);
    };

    @PostMapping
    @RequireAuth
    public ResponseEntity<GameResponse> createGame(
        @RequestBody GameRequest body,
        @AuthenticationPrincipal User user
    ) {
        Game createdGame = gameService.createGame(
                body,
                user
        );

        GameResponse response = new GameResponse(createdGame);

        return ResponseEntity.ok(response);
    };
};
