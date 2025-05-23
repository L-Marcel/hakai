package app.hakai.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.annotations.RequireAuth;
import app.hakai.backend.dtos.AnswerQuestionRequestBody;
import app.hakai.backend.dtos.ParticipantResponse;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.services.ParticipantService;
import app.hakai.backend.services.QuestionService;
import app.hakai.backend.transients.Participant;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private QuestionService questionService;
    
    @RequireAuth
    @GetMapping("/me")
    public ResponseEntity<ParticipantResponse> findParticipantByUser(
        @AuthenticationPrincipal User user
    ){
        Participant participant = participantService.findParticipantByUser(user);
        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity.ok(response);
    };

    @PostMapping("/answer")
    public ResponseEntity<Void> answerQuestion(
        @RequestBody AnswerQuestionRequestBody body
    ) {
        Question question = questionService.findQuestionById(
            body.getQuestion()
        );
        
        Participant participant = participantService.findParticipantByUuid(
            body.getParticipant()
        );

        participantService.answerQuestion(
            question, 
            participant, 
            body.getAnswer()
        );

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };

    @RequireAuth
    @DeleteMapping
    public ResponseEntity<Void> kickFromRoom(
        @AuthenticationPrincipal User user
    ) {
        Participant participant = participantService.findParticipantByUser(user);
        participantService.removeParticipant(participant);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };
};
