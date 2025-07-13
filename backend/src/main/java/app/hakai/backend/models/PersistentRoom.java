package app.hakai.backend.models;

import java.time.LocalDateTime;
import java.util.UUID;

import app.hakai.backend.enums.RoomStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersistentRoom {

    @Id
    private UUID gameId;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private LocalDateTime closingTime;
}