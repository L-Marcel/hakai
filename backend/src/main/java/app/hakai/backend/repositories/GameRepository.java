package app.hakai.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.hakai.backend.models.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByOwnerUuid(UUID owner);
};
