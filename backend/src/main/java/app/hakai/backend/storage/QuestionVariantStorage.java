package app.hakai.backend.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.kahai.framework.errors.FileError;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Component
public class QuestionVariantStorage extends Storage<List<QuestionVariant>> {
    private static final Logger log = LoggerFactory.getLogger(QuestionVariantStorage.class);

    @Autowired
    private ObjectMapper mapper;

    protected String getFilename(UUID uuid) {
        return uuid + ".json";
    }

    protected Path getPath(
            UUID uuid) {
        Path path = this.getStorageFolder("data/variants");
        return path.resolve(this.getFilename(uuid));
    };

    public void delete(UUID questionUuid) throws FileError {
        try {
            Path path = this.getPath(questionUuid);
            this.delete(path);
            log.info("Arquivo de variantes da questão ({}) deletado!", questionUuid);
        } catch (Exception e) {
            log.error(
                    "Erro ao apagar arquivo de variantes da questão ({}): {}",
                    questionUuid,
                    e.getMessage());
            throw new FileError();
        }
    }

    public void save(UUID questionUuid, List<QuestionVariant> variants) throws FileError {
        try {
            Path path = this.getPath(questionUuid);

            JavaType listType = this.mapper.getTypeFactory()
                    .constructCollectionType(List.class, QuestionVariant.class);

            String jsonContent = this.mapper
                    .writerFor(listType)
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(variants);

            Files.writeString(path, jsonContent);

            log.info("Arquivo de variantes da questão ({}) salvo com sucesso!", questionUuid);
        } catch (Exception e) {
            log.error("Erro ao escrever arquivo de variantes da questão ({}): {}", questionUuid, e.getMessage(), e);
            throw new FileError();
        }
    }

    public List<QuestionVariant> load(UUID questionUuid) throws FileError {
        Path path = this.getPath(questionUuid);
        File file = path.toFile();

        if (!file.exists()) {
            log.error("Arquivo de variantes da questão ({}) não foi encontrado!", questionUuid);
            throw new FileError();
        }

        try {
            List<QuestionVariant> variants = this.mapper.readValue(file, new TypeReference<List<QuestionVariant>>() {
            });
            log.info("Variantes da questão ({}) carregadas com sucesso!", questionUuid);
            return variants;
        } catch (IOException e) {
            log.error("Falha ao carregar ou parsear o arquivo de variantes da questão ({}): {}", questionUuid,
                    e.getMessage());
            throw new FileError();
        }
    }
}
