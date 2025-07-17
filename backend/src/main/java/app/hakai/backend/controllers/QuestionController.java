package app.hakai.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.annotations.RequireAuth;
import org.kahai.framework.dtos.request.SendQuestionVariantsRequest;
import org.kahai.framework.models.User;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.QuestionService;
import org.kahai.framework.services.RoomService;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.question.dtos.QuestionPayload;
import app.hakai.backend.question.dtos.SendAllQuestionsRequest;
import app.hakai.backend.strategies.VariantsDistributionAllByRandomly;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionService questionService;

    @PostConstruct
    public void setUpStrategies() {
        this.questionService.setDistributionStrategy(
            new VariantsDistributionAllByRandomly()
        );
    };

    @RequireAuth
    @PostMapping("/{uuid}/generate")
    public ResponseEntity<Void> startVariantsGeneration(
        @PathVariable UUID uuid,
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user);
        Question question = questionService.findQuestionById(uuid);
        questionService.startVariantsGeneration(question, room);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    };    

    @RequireAuth
    @PostMapping("/generate-all")
    public ResponseEntity<Void> startAllVariantsGeneration(
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user);
        questionService.startAllVariantsGeneration(room);

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .build();
    };

    @PostMapping("/send")
    public ResponseEntity<Void> sendVariantToParticipant(
        @RequestBody List<SendQuestionVariantsRequest> body
    ) {
        Room room = roomService.findRoomByCode(body.getFirst().getCode());

        for (SendQuestionVariantsRequest question : body) {
            questionService.sendVariant(
                    question.getVariants(),
                    question.getOriginal(),
                    room);
        }

        return ResponseEntity.ok().build();
    };

    @PostMapping("/send-all")
    public ResponseEntity<Void> sendAllVariantsToParticipants(
        @RequestBody SendAllQuestionsRequest body
    ) {
        Room room = roomService.findRoomByCode(body.getCode());

        Map<UUID, List<QuestionVariant>> mappedVariants = body.getQuestions().stream()
                .collect(Collectors.toMap(
                    QuestionPayload::getOriginal,
                    QuestionPayload::getVariants
                    ));

        questionService.sendAllVariant(mappedVariants, room);

        return ResponseEntity.ok().build();
    };
};
