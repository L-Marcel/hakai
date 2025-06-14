package app.hakai.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.annotations.RequireAuth;
import app.hakai.backend.dtos.request.SendQuestionVariantsRequestBody;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.services.QuestionService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Room;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionService questionService;

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
        Question original = questionService.findQuestionById(body.getOriginal());
        questionService.sendVariantByDifficulty(
            body,
            original,
            room
        );

        return ResponseEntity.ok().build();
    };
};
