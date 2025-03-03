package no.ntnu.idata2306.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = DifficultyLevel.class)
@Schema(description = "difficultyLevel of a course.", name = "difficulty_level")
@Entity
public class DifficultyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Schema(description = "difficultyLevel id")
    private int id;

    @Column(name = "level", nullable = false, unique = true, length = 35)
    @Schema(description = "difficultyLevel type")
    private String type;

    @OneToMany(mappedBy = "difficultyLevel")
    @Schema(description = "courses with the given difficultyLevel")
    private Set<Course> courses = new LinkedHashSet<>();
}
