package app.hakai.backend.services;

import org.springframework.stereotype.Service;

import app.hakai.backend.errors.AccessDenied;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;
import app.hakai.backend.transients.Room;

@Service
public class AccessControlService {
    public void checkGameOwnership(User user, Game game) {
        if(!game.getOwner().getUuid().equals(user.getUuid())) 
            throw new AccessDenied();
    };

    public void checkRoomOwnership(User user, Room room) {
        this.checkGameOwnership(user, room.getGame());
    };
};
