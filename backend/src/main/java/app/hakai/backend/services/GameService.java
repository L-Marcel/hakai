package app.hakai.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.GameNotFound;
import app.hakai.backend.models.Game;
import app.hakai.backend.repositories.GamesRepository;

@Service
public class GameService {
    @Autowired
    private GamesRepository repository;

    public Game getGame(UUID uuid) throws GameNotFound {
        if (uuid == null)
            throw new GameNotFound();
        return repository.findById(uuid).orElseThrow(
                () -> new GameNotFound());
    };

    public List<Game> getGamesToUser(UUID uuid) {
        return repository.findByOwnerUuid(uuid);
    }
};
