package app.hakai.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.annotations.RequireAuth;
import org.kahai.framework.dtos.response.ParticipantAnswerResponse;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.ParticipantAnswer;
import org.kahai.framework.models.User;
import org.kahai.framework.questions.Question;
import org.kahai.framework.services.GameService;
import org.kahai.framework.services.ParticipantAnswerService;
import org.kahai.framework.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/answers")
public class ParticipantAnswerController {
    @Autowired
    private ParticipantAnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GameService gameService;

    @RequireAuth
    @GetMapping("/question/{uuid}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByQuestion(
        @PathVariable UUID uuid
    ) {
        Question question = questionService.findQuestionById(uuid);
        List<ParticipantAnswer> answers = this.answerService.findAnswersByQuestion(question);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        return ResponseEntity.ok().body(response);
    };

    @RequireAuth
    @GetMapping("/game/{uuid}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByGame(
        @PathVariable UUID uuid
    ) {
        Game game = gameService.findGameById(uuid);
        List<ParticipantAnswer> answers = this.answerService.findAnswersByGame(game);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        return ResponseEntity.ok().body(response);
    };
    
    @RequireAuth
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByUser(
        @AuthenticationPrincipal User user
    ) {
        List<ParticipantAnswer> answers = this.answerService.findAnswersByUser(user);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        
        return ResponseEntity.ok(response);
    };
};
