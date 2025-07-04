package app.hakai.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.annotations.RequireAuth;
import org.kahai.framework.dtos.request.AnswerQuestionRequest;
import org.kahai.framework.dtos.response.ParticipantResponse;
import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.questions.Question;
import org.kahai.framework.services.ParticipantAnswerService;
import org.kahai.framework.services.ParticipantService;
import org.kahai.framework.services.QuestionService;
import org.kahai.framework.transients.Participant;
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

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantAnswerService answerService;

    @Autowired
    private QuestionService questionService;
    
    @RequireAuth
    @GetMapping("/me")
    public ResponseEntity<ParticipantResponse> findParticipantByUser(
        @AuthenticationPrincipal User user
    ) {
        Participant participant = participantService.findParticipantByUser(user);
        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity.ok(response);
    };

    @PostMapping("/answer")
    public ResponseEntity<Void> answerQuestion(
        @AuthenticationPrincipal User user,
        @RequestBody AnswerQuestionRequest body
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
            body.getAnswers()
        );

        UUID session = participant.getRoom().getSession();
        Game game = participant.getRoom().getGame();
        String nickname = participant.getNickname();
        
        List<Answer> answers = Answer.fromList(
            body.getAnswers()
        );

        answerService.createParticipantAnswer(
            session, 
            game, 
            user,
            question, 
            nickname,
            answers
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
