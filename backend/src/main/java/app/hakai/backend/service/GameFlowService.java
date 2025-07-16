package app.hakai.backend.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.kahai.framework.agents.AgentGenAICallback;
import org.kahai.framework.errors.RoomNotFound;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Game;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.GameService;
import org.kahai.framework.services.QuestionService;
import org.kahai.framework.services.RoomService;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.erros.RoomClosedException;
import app.hakai.backend.models.PersistentRoom;
import app.hakai.backend.repository.PersistentRoomRepository;
import app.hakai.backend.storage.QuestionVariantStorage;
import jakarta.transaction.Transactional;

@Service
public class GameFlowService {

    private static final Logger log = LoggerFactory.getLogger(GameFlowService.class);

    @Autowired
    private RoomService roomService;
    @Autowired
    private GameService gameService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private RoomEventPublisher roomEventPublisher;
    @Autowired
    private PersistentRoomRepository persistentRoomRepository;
    @Autowired
    private QuestionVariantStorage questionVariantStorage;

    public Room createAndPrepareRoom(Game game, Duration duration) {
        Room room = roomService.createRoom(game, duration);
        PersistentRoom persistentRoom = new PersistentRoom(room.getSession(), false, duration);
        persistentRoomRepository.save(persistentRoom);
        return room;
    }

    public void generateAndSaveVariants(Room room) {
        log.info("Disparando 'startAllVariantsGeneration' para a sala {}", room.getCode());

        Consumer<List<QuestionVariant>> onFinishCallback = (allGeneratedVariants) -> {
            log.info("Processo de geração concluído. Recebidas {} variantes no total.", allGeneratedVariants.size());

            Map<UUID, List<QuestionVariant>> variantsByQuestion = allGeneratedVariants.stream()
                    .collect(Collectors.groupingBy(variant -> variant.getRoot().getOriginal().getRoot().getUuid()));

            for (Map.Entry<UUID, List<QuestionVariant>> entry : variantsByQuestion.entrySet()) {
                questionVariantStorage.save(entry.getKey(), entry.getValue());
                log.info("-> Variantes da questão {} salvas.", entry.getKey());
            }

            PersistentRoom persistentRoom = findPersistentRoom(room.getSession());
            persistentRoom.setIsOpen(true);
            persistentRoomRepository.save(persistentRoom);

            log.info("Sala {} ABERTA para entrada de participantes.", room.getCode());
            roomEventPublisher.emitGenerationStatus(room, "COMPLETED");
        };

        questionService.startAllVariantsGeneration(room, onFinishCallback);
    }

    private void handleVariantGenerationResult(Question question, List<QuestionVariant> variants, Room room,
            AtomicInteger questionsRemaining) {

        questionVariantStorage.save(question.getRoot().getUuid(), variants);

        if (questionsRemaining.decrementAndGet() == 0) {
            PersistentRoom persistentRoom = findPersistentRoom(room.getSession());
            persistentRoom.setIsOpen(true);
            persistentRoomRepository.save(persistentRoom);

            roomEventPublisher.emitGenerationStatus(room, "COMPLETED");
        }
    }

    @Transactional
    public void startDistribution(Room room) {
        PersistentRoom persistentRoom = findPersistentRoom(room.getSession());

        persistentRoom.setIsOpen(false);
        persistentRoomRepository.save(persistentRoom);
        log.info("Sala {} fechada para novos participantes.", room.getCode());

        Duration duration = persistentRoom.getDuration();
        room.setDuration(duration);
        log.info("Duração da sala {} definida para {} minutos.", room.getCode(), duration);

        roomService.startRoomTimer(room);

        Game attachedGame = gameService.findGameById(room.getGame().getUuid());
        room.setGame(attachedGame);

        Map<UUID, List<QuestionVariant>> mappedVariants = new ConcurrentHashMap<>();
        for (Question question : attachedGame.getQuestions()) {
            List<QuestionVariant> variants = questionVariantStorage.load(question.getRoot().getUuid());
            mappedVariants.put(question.getRoot().getUuid(), variants);
        }

        if (mappedVariants.isEmpty()) {
            log.error("NENHUMA VARIANTE encontrada no storage para a sala {}. O jogo não pode começar.",
                    room.getCode());
            roomService.stopRoomTimer(room);
            return;
        }

        log.info("Iniciando distribuição de variantes para a sala {}", room.getCode());
        questionService.sendAllVariant(mappedVariants, room);
    }

    public void checkIfRoomIsOpen(UUID roomId) {
        PersistentRoom persistentRoom = findPersistentRoom(roomId);
        if (!persistentRoom.getIsOpen()) {
            throw new RoomClosedException("A sala não está aberta para novos participantes.");
        }
    }

    private PersistentRoom findPersistentRoom(UUID roomId) {
        return persistentRoomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFound());
    }
}