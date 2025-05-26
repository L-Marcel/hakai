package app.hakai.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.hakai.backend.models.Context;

@Repository
public interface ContextRepository extends  JpaRepository<Context, UUID>{
    List<Context> findByNameIn(List<String> names);
}
