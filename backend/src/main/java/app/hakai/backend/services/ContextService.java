package app.hakai.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.hakai.backend.models.Context;
import app.hakai.backend.repositories.ContextRepository;

@Service
public class ContextService {
    @Autowired
    private ContextRepository contextRepository;


    public List<Context> findByName(List<String> contextos) {

        return contextRepository.findByNameIn(contextos);
        
    }
}
