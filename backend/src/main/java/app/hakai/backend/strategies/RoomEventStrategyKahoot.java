package app.hakai.backend.strategies;

import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import lombok.Setter;

@Setter
public class RoomEventStrategyKahoot implements RoomEventStrategy {
    
    private RoomRepository roomRepository;

    private RoomEventPublisher roomEventPublisher;

    @Override
    public void onClose(Room room) {
        this.roomRepository.remove(room);
    }

    @Override
    public void onStart(Room room) {
        
    }

    @Override
    public void onDurationExceeded(Room room) {
        // TODO Auto-generated method stub
    }    
}
