package app.hakai.backend.models;

import java.util.LinkedList;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Game {
    @Getter
    @Setter
    @Id
    @GeneratedValue
    private UUID uuid;

    @Getter
    @Setter
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private LinkedList<Question> questions = new LinkedList<Question>();
};
