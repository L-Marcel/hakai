package app.hakai.backend.services;

import org.springframework.stereotype.Service;

import app.hakai.backend.errors.AccessDenied;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.User;

@Service
public class AccessControlService {
    public void checkGameOwnership(User user, Game game) {
        if (!game.getOwner().getUuid().equals(user.getUuid())) 
            throw new AccessDenied();
    };
};
