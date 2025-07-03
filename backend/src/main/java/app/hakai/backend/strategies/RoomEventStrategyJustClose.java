package app.hakai.backend.strategies;

import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;

public class RoomEventStrategyJustClose implements RoomEventStrategy {
    private RoomRepository repository;

    @Override
    public void onClose(Room room) {
        this.repository.remove(room);
    };

    @Override
    public void onStart(Room room) {};

    @Override
    public void onDurationExceeded(Room room) {};

    @Override
    public void setRoomEventPublisher(RoomEventPublisher publisher) {};

    @Override
    public void setRoomRepository(RoomRepository repository) {
        this.repository = repository;
    };
};
