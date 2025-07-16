package app.hakai.backend.strategies;

import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.kahai.framework.errors.RoomNotFound;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.services.GameService;
import org.kahai.framework.services.QuestionService;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import app.hakai.backend.models.PersistentRoom;
import app.hakai.backend.repository.PersistentRoomRepository;
import app.hakai.backend.storage.QuestionVariantStorage;
import app.hakai.backend.service.PersistentRoomService;

public class ProvaiRoomEventStrategy implements RoomEventStrategy {

    private static final Logger log = LoggerFactory.getLogger(ProvaiRoomEventStrategy.class);

    private final QuestionService questionService;
    private final QuestionVariantStorage questionVariantStorage;
    private final PersistentRoomRepository persistentRoomRepository;
    private final GameService gameService;

    public ProvaiRoomEventStrategy(
            QuestionService questionService,
            QuestionVariantStorage questionVariantStorage,
            PersistentRoomRepository persistentRoomRepository, GameService gameService) {
        this.questionService = questionService;
        this.questionVariantStorage = questionVariantStorage;
        this.persistentRoomRepository = persistentRoomRepository;
        this.gameService = gameService;

    }

    @Override
    @Transactional
    public void onStart(Room room) {
        log.info("Estratégia 'Provai' ativada para o evento onStart da sala {}", room.getCode());

        PersistentRoom persistentRoom = persistentRoomRepository.findById(room.getSession())
                .orElseThrow(() -> new RoomNotFound());
        persistentRoom.setIsOpen(false);
        persistentRoomRepository.save(persistentRoom);
        log.info("Sala {} fechada para novos participantes.", room.getCode());

        Map<UUID, List<QuestionVariant>> mappedVariants = new ConcurrentHashMap<>();
        for (Question question : gameService.findGameById(room.getGame().getUuid()).getQuestions()) {
            List<QuestionVariant> variants = questionVariantStorage.load(question.getRoot().getUuid());
            mappedVariants.put(question.getRoot().getUuid(), variants);
        }

        if (mappedVariants.isEmpty()) {
            log.error("Nenhuma variante encontrada no storage para a sala {}. O jogo não pode começar.",
                    room.getCode());

            throw new IllegalStateException(
                    "Não foi possível iniciar a sala " + room.getCode() + " por falta de variantes.");
        }
        log.info("Estratégia 'Provai' iniciando a distribuição de variantes para {} questões...",
                mappedVariants.size());
        questionService.sendAllVariant(mappedVariants, room);
    }

    @Override
    public void onClose(Room room) {
        log.info("Estratégia 'Provai': Sala {} foi fechada.", room.getCode());

    }

    @Override
    public void onDurationExceeded(Room room) {
        log.info("Estratégia 'Provai': O tempo da sala {} acabou.", room.getCode());

    }
}