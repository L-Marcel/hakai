package app.hakai.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.hakai.backend.enums.RoomStatus;
import app.hakai.backend.models.PersistentRoom;

@Repository
public interface PersistentRoomRepository extends JpaRepository<PersistentRoom, UUID> {

    List<PersistentRoom> findByStatusAndClosingTimeBefore(RoomStatus status, LocalDateTime currentTime);
}