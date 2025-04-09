package app.hakai.backend.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.Game;
import app.hakai.backend.repositories.GameRepository;

@Service
public class GameService {
    @Autowired
    private GameRepository repository;

    public Optional<Game> getGame(UUID uuid){
        return repository.findById(uuid);
    }
    
}
