package app.hakai.backend.repositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Repository;

import app.hakai.backend.models.User;
import app.hakai.backend.transients.Participant;

@Repository
public class ParticipantRepository {
    private List<Participant> participants = Collections.synchronizedList(
        new LinkedList<Participant>()
    );

    public void add(Participant participant) {
        this.participants.add(participant);
    };

    public void remove(Participant participant) {
        this.participants.remove(participant);
    };

    private Optional<Participant> find(Function<Participant, Boolean> search) {
        synchronized(this.participants) {
            for(Participant participant : this.participants) {
                if(search.apply(participant))
                    return Optional.of(participant);
            };
    
            return Optional.empty();
        }
    };

    public Optional<Participant> findByUser(User user) {
        return this.find((participant) -> {
            return participant.getUser().isPresent() && 
                participant.getUser().get().getUuid().equals(user.getUuid());
        });
    };

    public Optional<Participant> findByUuid(UUID uuid) {
        return this.find((participant) -> {
            return participant.getUuid().equals(uuid);
        });
    };
};
