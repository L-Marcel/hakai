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
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Component
public class QuestionVariantStorage extends Storage<List<QuestionVariant>> {
    private static final Logger log = LoggerFactory.getLogger(QuestionVariantStorage.class);

    private static final String STORAGE_FOLDER = "variants";
    @Autowired
    private ObjectMapper mapper;

    protected String getFilename(UUID uuid) {
        return uuid + ".json";
    }

    protected Path getPath(UUID questionUuid) {
        Path path = this.getStorageFolder(STORAGE_FOLDER);
        return path.resolve(this.getFilename(questionUuid));
    }

    public void delete(UUID questionUuid) throws FileError {
        try {
            Path path = this.getPath(questionUuid);
            this.delete(path); // Método herdado da classe Storage base
            log.info("Arquivo de variantes da questão ({}) deletado!", questionUuid);
        } catch (Exception e) {
            log.error(
                    "Erro ao apagar arquivo de variantes da questão ({}): {}",
                    questionUuid,
                    e.getMessage());
            throw new FileError();
        }
    }

    /**
     * Salva uma lista de QuestionVariant em um arquivo JSON.
     * 
     * @param questionUuid O UUID da questão original, usado para nomear o arquivo.
     * @param variants     A lista de variantes a ser salva.
     * @throws FileError se ocorrer um erro ao escrever o arquivo.
     */
    public void save(UUID questionUuid, List<QuestionVariant> variants) throws FileError {
        try {
            Path path = this.getPath(questionUuid);
            this.write(path, variants); // Método herdado que serializa o objeto para JSON
            log.info("Arquivo de variantes da questão ({}) escrito com sucesso!", questionUuid);
        } catch (Exception e) {
            log.error(
                    "Erro ao escrever arquivo de variantes da questão ({}): {}",
                    questionUuid,
                    e.getMessage());
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
            // 2. Use a instância do 'mapper' injetada, em vez de 'getMapper()'
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
