package app.hakai.backend.services;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.ParticipantNotFound;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;

@Service
public class ParticipantService {
    @Autowired
    private MessagingService messagingService;

    public Participant createParticipant(String nickname, User user) {
        Participant participant = new Participant(nickname);
        if(user != null) participant.setUser(Optional.of(user));
        
        return participant;
    };

    private Optional<Participant> find(
        Room room, 
        Function<Participant, Boolean> search
    ) {
        for(Participant participant : room.getParticipants()) {
            if(search.apply(participant))
                return Optional.of(participant);
        };

        return Optional.empty();
    };

    public Participant findParticipantByUser(
        Room room, 
        User user
    ) throws ParticipantNotFound {
        return this.find(
            room, 
            (Participant participant) -> participant.getUser().isPresent() && participant.getUser().get().getUuid().equals(user.getUuid())
        ).orElseThrow(ParticipantNotFound::new);
    };

    public Participant findParticipantByUuid(
        Room room, 
        UUID uuid
    ) throws ParticipantNotFound {
        return this.find(
            room, 
            (Participant participant) -> participant.getUuid().equals(uuid)
        ).orElseThrow(ParticipantNotFound::new);
    };

    public void answerQuestion(
        Question question,
        Participant participant,
        String answer
    ) {
        boolean isCorrect = question.getAnswer().equals(answer);

        if(isCorrect) {
            participant.incrementScore();
            participant.incrementCorrectAnswers();
        } else {
            participant.incrementWrongAnswers();
        };
    };
};
