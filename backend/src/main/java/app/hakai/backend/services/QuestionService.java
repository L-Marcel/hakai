package app.hakai.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.agents.PedagogicalAgent;
import app.hakai.backend.dtos.request.CreateQuestionRequestBody;
import app.hakai.backend.dtos.request.SendQuestionVariantsRequestBody;
import app.hakai.backend.errors.QuestionNotFound;
import app.hakai.backend.events.RoomEventPublisher;
import app.hakai.backend.models.Context;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.repositories.ContextRepository;
import app.hakai.backend.repositories.QuestionRepository;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.QuestionVariant;
import app.hakai.backend.transients.Room;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ContextRepository contextRepository;

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

    public List<Question> createAllQuestions(
        Game game, 
        List<CreateQuestionRequestBody> bodies
    ) {
        List<Question> questions = new ArrayList<>();

        for (CreateQuestionRequestBody body : bodies) {
            Question question = new Question();
            question.setGame(game);
            question.setQuestion(body.getQuestion());
            question.setAnswer(body.getAnswer());

            List<String> contextNames = body.getContext();
            if (contextNames != null && !contextNames.isEmpty()) {
                List<Context> contexts = contextNames
                    .stream()
                    .map(Context::new)
                    .collect(Collectors.toList());

                for(Context context : contexts) {
                    if(!contextRepository.existsByName(context.getName())) {
                        contextRepository.save(context);
                    };
                };
                
                question.setContexts(contexts);
            };

            questions.add(question);
        };

        questionRepository.saveAll(questions);

        return questions;
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
