package app.hakai.backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import app.hakai.backend.models.User;
import app.hakai.backend.transients.Participant;

@Service
public class ParticipantService {
    public Participant createParticipant(String nickname, User user) {
        Participant participant = new Participant(nickname);
        if(user != null) participant.setUser(Optional.of(user));
        
        return participant;
    };
};
