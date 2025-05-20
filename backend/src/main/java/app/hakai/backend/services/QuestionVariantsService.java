package app.hakai.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.agents.PedagogicalAgent;
import app.hakai.backend.models.Question;
import app.hakai.backend.transients.Room;

@Service
public class QuestionVariantsService {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PedagogicalAgent pedagogicalAgent;

    public void startVariantsGeneration(UUID uuid, Room room) {
        Question question = questionService.findQuestionById(uuid);
        pedagogicalAgent.generateRoomQuestionsVariants(question, variants -> {
            messagingService.sendVariantsToOwner(room, variants);
        });
    };
};
