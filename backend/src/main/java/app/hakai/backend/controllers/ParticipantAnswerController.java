package app.hakai.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.dtos.request.AnswerQuestionRequestBody;
import org.kahai.framework.dtos.response.ParticipantAnswerResponse;
import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.ParticipantAnswer;
import org.kahai.framework.models.Question;
import org.kahai.framework.services.GameService;
import org.kahai.framework.services.ParticipantAnswerService;
import org.kahai.framework.services.ParticipantService;
import org.kahai.framework.services.QuestionService;
import org.kahai.framework.transients.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/answers")
public class ParticipantAnswerController {
    @Autowired
    private ParticipantAnswerService answerService;
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<ParticipantAnswer> createAnswer(@RequestBody AnswerQuestionRequestBody body) {

        Participant participant = participantService.findParticipantByUuid(body.getParticipant());

        UUID session =  participant.getRoom().getSession();
        String nickname = participant.getNickname();
        Question question = questionService.findQuestionById(body.getQuestion());
        Game game = question.getGame();
        List<Answer> answers = ParticipantAnswer.convertStringsToAnswers(body.getAnswers());
        
        ParticipantAnswer answer = new ParticipantAnswer(session, game, question, nickname, answers);

        answerService.saveParticipantAnswer(answer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    };

    @GetMapping("/question/{uuid}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByQuestion(
        @PathVariable UUID uuid
    ) {
        Question question = questionService.findQuestionById(uuid);
        List<ParticipantAnswer> answers = answerService.findAnswersByQuestion(question);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/game/{uuid}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByGame(
        @PathVariable UUID uuid
    ) {
        Game game = gameService.findGameById(uuid);
        List<ParticipantAnswer> answers = answerService.findAnswersByGame(game);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        return ResponseEntity.ok().body(response);
    };
    
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByNickname(
        @PathVariable String nickname
    ) {
        List<ParticipantAnswer> answers = answerService.findAnswersByNickname(nickname);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        
        return ResponseEntity.ok(response);
    };

    @GetMapping("/nickname-in-game/{gameUuid}/{nickname}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByNicknameInGame(
        @RequestParam String nickname,
        @RequestParam UUID gameUuid
    ) {
        Game game = gameService.findGameById(gameUuid);
        List<ParticipantAnswer> answers = answerService.findAnswersByNicknameInGame(nickname, game);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        return ResponseEntity.ok(response);
    };

    @GetMapping("nickname-in-question/{questionUuid}/{nickname}")
    public ResponseEntity<List<ParticipantAnswerResponse>> getAnswersByNicknameInQuestion(
        @RequestParam String nickname,
        @RequestParam UUID questionUuid
    ) {
        Question question = questionService.findQuestionById(questionUuid);
        List<ParticipantAnswer> answers = answerService.findAnswersByNicknameInQuestion(nickname, question);
        List<ParticipantAnswerResponse> response = ParticipantAnswerResponse.mapFromList(answers);
        return ResponseEntity.ok(response);
    };

}
