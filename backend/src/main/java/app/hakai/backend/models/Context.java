package app.hakai.backend.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contexts")
public class Context {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable=false)
    private String name;

    @ManyToMany(mappedBy = "contexts")
    @JsonIgnore 
    private List<Question> questions = new ArrayList<>();

    public Context(String name) {
        this.name = name;
    };
};
