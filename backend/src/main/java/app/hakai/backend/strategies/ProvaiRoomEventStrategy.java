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

    public ProvaiRoomEventStrategy(
            QuestionService questionService,
            QuestionVariantStorage questionVariantStorage,
            PersistentRoomRepository persistentRoomRepository) {
        this.questionService = questionService;
        this.questionVariantStorage = questionVariantStorage;
        this.persistentRoomRepository = persistentRoomRepository;
    }

    @Override
    @Transactional // Essencial para evitar LazyInitializationException ao acessar
                   // room.getGame().getQuestions()
    public void onStart(Room room) {
        log.info("Estratégia 'Provai' ativada para o evento onStart da sala {}", room.getCode());

        // 1. Fecha a sala para que ninguém mais entre no meio do jogo.
        PersistentRoom persistentRoom = persistentRoomRepository.findById(room.getSession())
                .orElseThrow(() -> new RoomNotFound());
        persistentRoom.setIsOpen(false);
        persistentRoomRepository.save(persistentRoom);
        log.info("Sala {} fechada para novos participantes.", room.getCode());

        // 2. Monta o mapa de variantes, carregando do nosso storage.
        Map<UUID, List<QuestionVariant>> mappedVariants = new ConcurrentHashMap<>();
        // O @Transactional garante que esta linha funcione sem erros de Lazy Loading.
        for (Question question : room.getGame().getQuestions()) {
            List<QuestionVariant> variants = questionVariantStorage.load(question.getRoot().getUuid());
            mappedVariants.put(question.getRoot().getUuid(), variants);
        }

        // 3. Verifica se alguma variante foi carregada.
        if (mappedVariants.isEmpty()) {
            log.error("Nenhuma variante encontrada no storage para a sala {}. O jogo não pode começar.",
                    room.getCode());
            // Importante: Precisamos parar o timer se o jogo não puder começar.
            // Como a estratégia não tem acesso ao RoomService, essa lógica fica no
            // controller.
            // Lançar uma exceção aqui seria uma boa prática.
            throw new IllegalStateException(
                    "Não foi possível iniciar a sala " + room.getCode() + " por falta de variantes.");
        }

        // 4. Usa o método de distribuição do framework para enviar as questões.
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