package app.hakai.backend.controllers;

import java.util.UUID;

import org.kahai.framework.annotations.RequireAuth;
import org.kahai.framework.dtos.request.SendQuestionVariantsRequestBody;
import org.kahai.framework.models.Question;
import org.kahai.framework.models.User;
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

import app.hakai.backend.strategies.VariantsDistributionByDifficulty;
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
            new VariantsDistributionByDifficulty()
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

    @PostMapping("/send")
    public ResponseEntity<Void> sendVariantToParticipant(
        @RequestBody SendQuestionVariantsRequestBody body
    ) {
        Room room = roomService.findRoomByCode(body.getCode());
        questionService.sendVariant(
            body.getVariants(),
            body.getOriginal(),
            room
        );

        return ResponseEntity.ok().build();
    };
};
