package app.hakai.backend.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "game", nullable = false)
    private Game game;

    @Column(nullable = false, length = 600)
    private String question;

    @Column(nullable = false, length = 200)
    private String answer;
@ManyToMany
@JoinTable(
    name = "context_question",
    joinColumns = @JoinColumn(name = "question_id"),
    inverseJoinColumns = @JoinColumn(name = "context_id")
)
    private List<Context> contexts = new ArrayList<>();
   
};
