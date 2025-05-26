package app.hakai.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.Context;
import app.hakai.backend.repositories.ContextRepository;

@Service
public class ContextService {
    @Autowired
    private ContextRepository contextRepository;


    public Context findByName(String contexto) {

        return contextRepository.findByName(contexto);

    }

    public Context save(Context context) {
        return contextRepository.save(context);
    }
}
