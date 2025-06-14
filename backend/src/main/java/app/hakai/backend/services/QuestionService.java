package app.hakai.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.agents.PedagogicalAgent;
import app.hakai.backend.dtos.request.SendQuestionVariantsRequestBody;
import app.hakai.backend.errors.QuestionNotFound;
import app.hakai.backend.events.RoomEventPublisher;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.repositories.QuestionRepository;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.QuestionVariant;
import app.hakai.backend.transients.Room;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    @Autowired
    private PedagogicalAgent pedagogicalAgent;
  
    public List<Question> findQuestionsByGame(Game game) {
        return this.questionRepository.findAllByGame(game);
    };

    public Question findQuestionById(UUID uuid) throws QuestionNotFound {
        return this.questionRepository.findByUuid(uuid)
            .orElseThrow(QuestionNotFound::new);
    };

    public void startVariantsGeneration(Question question, Room room) {
        this.pedagogicalAgent.generateRoomQuestionsVariants(
            question,
            room, 
            (variants) -> {
                this.roomEventPublisher.emitVariantsGenerated(room, variants);
            }
        );
    };

    public void sendVariantByDifficulty( 
        SendQuestionVariantsRequestBody body,
        Question original,
        Room room
    ) {
        List<Participant> participants = room.getParticipants();
        List<QuestionVariant> variants = body.getVariants();

        synchronized(participants) {
            for (Participant participant : participants) {
                Optional<QuestionVariant> selected = variants.stream()
                    .filter(
                        (variant) -> variant.getDifficulty() == participant.getCurrentDifficulty()
                    ).findFirst();
    
                if(selected.isEmpty()) continue;
                
                selected.get().setOriginal(original);
                roomEventPublisher.emitVariantIntended(
                    room, 
                    participant.getUuid(),
                    selected.get()
                );
            };
        }
    };
};
