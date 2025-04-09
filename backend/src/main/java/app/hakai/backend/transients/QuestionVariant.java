package app.hakai.backend.transients;

import java.util.List;

import app.hakai.backend.models.Question;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QuestionVariant {
    @Column(nullable = false)
    private int difficulty = 0;

    
    @ElementCollection
    @CollectionTable(name = "variant_options", joinColumns = @JoinColumn(name = "variant_id"))
    @Column(name = "option")
    private List<String> options;
    

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question originalQuestion;
}
