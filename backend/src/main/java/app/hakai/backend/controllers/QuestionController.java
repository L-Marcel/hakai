package app.hakai.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.hakai.backend.annotations.RequireAuth;
import app.hakai.backend.dtos.QuestionVariantRequestBody;
import app.hakai.backend.dtos.QuestionVariantResponse;
import app.hakai.backend.models.Difficulty;
import app.hakai.backend.models.Question;
import app.hakai.backend.models.User;
import app.hakai.backend.services.ParticipantService;
import app.hakai.backend.services.QuestionService;
import app.hakai.backend.services.RoomService;
import app.hakai.backend.transients.Participant;
import app.hakai.backend.transients.QuestionVariant;
import app.hakai.backend.transients.Room;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ParticipantService participantService;
    
    @Autowired
    private SimpMessagingTemplate simp;

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

    
    @PostMapping("/send-to-participants/{code}")
public ResponseEntity<Void> sendVariantByDifficulty(
    @PathVariable String code,
    @RequestBody List<QuestionVariantResponse> variants
) {
    System.out.println("Enviando questão para participantes");

    Room room = roomService.findRoomByCode(code);
    if (room == null) return ResponseEntity.notFound().build();

    List<Participant> participants = room.getParticipants();

    for (Participant p : participants) {
        Difficulty currentDifficulty = p.getCurrentDifficulty();

        QuestionVariantResponse matched = variants.stream()
            .filter(v -> v.getDifficulty() == Difficulty.mapDifficultyEnumToInt(currentDifficulty))
            .findFirst()
            .orElse(null);

        if (matched == null) continue;

        Question question = questionService.findQuestionById(matched.getOriginal());

        QuestionVariant variant = new QuestionVariant();
        variant.setUuid(matched.getUuid());
        variant.setDifficulty(matched.getDifficulty());
        variant.setQuestion(matched.getQuestion());
        variant.setOptions(matched.getOptions());
        variant.setOriginal(question);

        simp.convertAndSend(
            "/channel/events/rooms/" + code + "/participants/" + p.getUuid() + "/question",
            new QuestionVariantResponse(variant, false) // se quiser esconder a resposta
        );
    }

    return ResponseEntity.ok().build();
}

};
