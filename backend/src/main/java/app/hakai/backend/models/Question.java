package app.hakai.backend.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Question {
    @Getter
    @Setter
    @Id
    @GeneratedValue
    private UUID uuid;
};
