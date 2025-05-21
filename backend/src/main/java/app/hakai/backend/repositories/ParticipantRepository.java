package app.hakai.backend.repositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Repository;

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
        synchronized (this.participants) {
            for(Participant participant : this.participants) {
                if(search.apply(participant))
                    return Optional.of(participant);
            };
    
            return Optional.empty();
        }
    };

    public Optional<Participant> findByUser(UUID user) {
        return this.find((Participant participant) -> {
            return participant.getUser().isPresent() && 
                participant.getUser().get().getUuid().equals(user);
        });
    };

    public Optional<Participant> findByUuid(UUID uuid) {
        return this.find((Participant participant) -> {
            return participant.getUuid().equals(uuid);
        });
    };
};
