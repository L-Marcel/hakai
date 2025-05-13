package app.hakai.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import app.hakai.backend.dtos.RoomResponse;
import app.hakai.backend.agents.PedagogicalAgent;
import app.hakai.backend.dtos.CreateRoomRequestBody;
import app.hakai.backend.dtos.JoinRoomRequestBody;
import app.hakai.backend.dtos.ParticipantResponse;
import app.hakai.backend.models.Game;
import app.hakai.backend.models.Question;
import app.hakai.backend.services.GameService;
import app.hakai.backend.services.MessagingService;
import app.hakai.backend.services.QuestionService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.Room;

@Controller
@RequestMapping("/rooms")
@MessageMapping
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PedagogicalAgent pedagogicalAgent;

    @PostMapping("/create")
    public ResponseEntity<RoomResponse> create(
        @RequestBody CreateRoomRequestBody body
    ) {
        Game game = gameService.getGame(body.getGame());
        Room createdRoom = roomService.createRoom(game);
        RoomResponse response = new RoomResponse(createdRoom);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };

    @GetMapping("/{code}")
    public ResponseEntity<RoomResponse> get(
        @PathVariable String code
    ){
        Room room = roomService.getRoom(code);
        RoomResponse response = new RoomResponse(room);
        return ResponseEntity.ok().body(response);
    };

    @PostMapping("/{code}/close")
    public ResponseEntity<String> close(
        @PathVariable String code
    ) {
        Room room = roomService.getRoom(code);
        roomService.closeRoom(room);
        messagingService.closeRoomToAll(room);
        return ResponseEntity.ok().body("Sala fechada!");
    };

    @PostMapping("/{code}/join")
    public ResponseEntity<ParticipantResponse> join(
        @PathVariable String code,
        @RequestBody JoinRoomRequestBody body
    ) {
        Room room = roomService.getRoom(code);
        Participant participant = new Participant(body.getNickname());
        roomService.joinRoom(room, participant);
        messagingService.sendRoomToAll(room);
        ParticipantResponse response = new ParticipantResponse(participant);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };

    @MessageMapping("/{code}/{participant}")
    public void participant(
        @DestinationVariable String code, 
        @DestinationVariable UUID participant
    ) {
        Room room = roomService.getRoom(code);
        messagingService.sendRoomToParticipant(room, participant);
    };

    @PostMapping("/{code}/question/{uuid}/generate")
    public ResponseEntity<Void> requestToGenerate(@PathVariable String code, @PathVariable UUID uuid) {
        Question question = questionService.getQuestionById(uuid);
        Room room = roomService.getRoom(code);
        
        pedagogicalAgent.generateRoomQuestionsVariants(question, variants -> {
            messagingService.sendVariantsToOwner(room, variants);
        });
        return ResponseEntity.ok().build();
    };
};