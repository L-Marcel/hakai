package app.hakai.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.repositories.GameRepository;

@Service
public class GameService {
    @Autowired
    private GameRepository repository;

    public Game findGameById(UUID uuid) throws GameNotFound {
        return this.repository.findById(uuid)
            .orElseThrow(GameNotFound::new);
    };

    public List<Game> findGamesByUser(UUID uuid) {
        return this.repository.findByOwnerUuid(uuid);
    };
};
