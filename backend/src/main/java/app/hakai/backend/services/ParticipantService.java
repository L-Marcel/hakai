package app.hakai.backend.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.errors.ParticipantAlreadyInRoom;
import app.hakai.backend.errors.ParticipantNotFound;
import app.hakai.backend.events.RoomEventPublisher;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.repositories.ParticipantRepository;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    private boolean isParticipantAlreadyInRoom(
        Room room, 
        Participant candidate
    ) {
        for(Participant participant : room.getParticipants()) {
            String nickname = participant.getNickname().toUpperCase();
            String candidateNickname = candidate.getNickname().toUpperCase();
            boolean nicknameAlreadyInUse = nickname.equals(candidateNickname);
            boolean idAlreadyInUse = participant.getUuid().equals(candidate.getUuid());
            boolean userAlreadyInUse = false;
            Optional<User> user = participant.getUser();
            Optional<User> candidateUser = candidate.getUser();
            if(user.isPresent() && candidateUser.isPresent()) {
                userAlreadyInUse = user.get().getEmail().equals(
                    candidateUser.get().getEmail()
                );
            };

            if(nicknameAlreadyInUse || idAlreadyInUse || userAlreadyInUse) 
                return true;
        };

        return false;
    };
    
    public Participant createParticipant(
        String nickname, 
        Room room, 
        User user
    ) throws ParticipantAlreadyInRoom {
        Participant participant;

        if(user != null) participant = new Participant(nickname, room, user);
        else participant = new Participant(nickname, room);

        synchronized (room.getParticipants()) {
            boolean alreadyInRoom = this.isParticipantAlreadyInRoom(room, participant);
            if(alreadyInRoom) throw new ParticipantAlreadyInRoom();

            this.repository.add(participant);
            room.getParticipants().add(participant);
        }

        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());

        return participant;
    };

    public Participant findParticipantByUser(
        User user
    ) throws ParticipantNotFound {
        return this.repository.findByUser(user)
            .orElseThrow(ParticipantNotFound::new);
    };

    public Participant findParticipantByUuid(
        UUID uuid
    ) throws ParticipantNotFound {
        return this.repository.findByUuid(uuid)
            .orElseThrow(ParticipantNotFound::new);
    };

    public void removeAllByRoom(Room room) {
        synchronized (room.getParticipants()) {
            for (Participant participant : room.getParticipants()) {
                this.repository.remove(participant);
            };
        };
    };

    public void removeParticipant(Participant participant) {
        synchronized (participant.getRoom().getParticipants()) {
            participant.getRoom().getParticipants().remove(participant);
            this.repository.remove(participant);
        };

        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());
    };

    public void answerQuestion(
        Question question,
        Participant participant,
        String answer
    ) {
        boolean isCorrect = question.getAnswer().equals(answer);

        synchronized(participant) {
            if(isCorrect) {
                participant.incrementScore();
                participant.incrementCorrectAnswers();
            } else {
                participant.incrementWrongAnswers();
            };
        }

        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());
    };
};
